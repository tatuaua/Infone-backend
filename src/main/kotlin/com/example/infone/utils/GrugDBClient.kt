package com.example.infone.utils

import org.slf4j.LoggerFactory
import java.io.*

class GrugDBClient private constructor() {
    companion object {
        private val singletonInstance: GrugDBClient by lazy { GrugDBClient() }
        private val databaseDirectory = File("grug_db")
        private val logger = LoggerFactory.getLogger(GrugDBClient::class.java)

        /**
         * Retrieves or creates a singleton instance of GrugDBClient.
         *
         * @return The singleton instance of GrugDBClient.
         */
        fun getInstance(): GrugDBClient {
            if (databaseDirectory.exists()) {
                logger.debug("Database directory already created")
            } else {
                if (!databaseDirectory.mkdirs()) {
                    logger.error("Failed to create database directory")
                }
            }
            return singletonInstance
        }

        /**
         * Clears all files in the database directory, which empties the database and removes all stored entities
         */
        fun clearDatabaseDirectory() {
            databaseDirectory.listFiles()?.forEach { storedFile ->
                if (!storedFile.delete()) {
                    logger.error("Failed to delete database file {}", storedFile.name)
                }
            }
        }
    }

    init {
        logger.info("GrugDBClient instance created")
    }

    /**
     * Persists a single entity to the Grug database by appending it to the appropriate collection.
     */
    @Synchronized
    fun <T : Any> save(entity: T) {
        val entityClass = entity::class.java
        val entityTypeName = entityClass.simpleName.lowercase()
        val storageFile = File(databaseDirectory, "grug_$entityTypeName.ser")

        val entityList = loadCollection(entityClass, storageFile).toMutableList()
        entityList.add(entity)

        FileOutputStream(storageFile).use { fileOutput ->
            ObjectOutputStream(fileOutput).use { objectOutput ->
                objectOutput.writeObject(entityList)
            }
        }
    }

    /**
     * Updates all entities of a given type in the Grug database that match the specified condition.
     */
    @Synchronized
    fun <T : Any> update(
        entityClass: Class<T>,
        condition: (T) -> Boolean,
        update: (T) -> Unit
    ) {
        val entityTypeName = entityClass.simpleName.lowercase()
        val storageFile = File(databaseDirectory, "grug_$entityTypeName.ser")

        val entityList = loadCollection(entityClass, storageFile).toMutableList()
        entityList.filter(condition).forEach(update)

        FileOutputStream(storageFile).use { fileOutput ->
            ObjectOutputStream(fileOutput).use { objectOutput ->
                objectOutput.writeObject(entityList)
            }
        }
    }

    /**
     * Updates the first entity matching the condition or saves the entity if no match is found.
     */
    @Synchronized
    fun <T : Any> updateOrSave(
        entity: T,
        condition: (T) -> Boolean,
        update: (T) -> Unit
    ) {
        val entityClass = entity::class.java
        val entityTypeName = entityClass.simpleName.lowercase()
        val storageFile = File(databaseDirectory, "grug_$entityTypeName.ser")

        val entityList = loadCollection(entityClass, storageFile).toMutableList()
        val existingEntity = entityList.find(condition)

        if (existingEntity != null) {
            update(existingEntity)
        } else {
            entityList.add(entity)
        }

        FileOutputStream(storageFile).use { fileOutput ->
            ObjectOutputStream(fileOutput).use { objectOutput ->
                objectOutput.writeObject(entityList)
            }
        }
    }

    /**
     * Persists a batch of entities of the same type to the Grug database.
     */
    @Synchronized
    fun <T : Any> saveBatch(entities: List<T>) {
        val entityClass = entities.first()::class.java
        val entityTypeName = entityClass.simpleName.lowercase()
        val storageFile = File(databaseDirectory, "grug_$entityTypeName.ser")

        val existingEntityList = loadCollection(entityClass, storageFile).toMutableList()
        existingEntityList.addAll(entities)

        FileOutputStream(storageFile).use { fileOutput ->
            ObjectOutputStream(fileOutput).use { objectOutput ->
                objectOutput.writeObject(existingEntityList)
            }
        }
    }

    /**
     * Retrieves all entities of a given type from the Grug database.
     */
    @Synchronized
    fun <T : Any> find(entityClass: Class<T>): List<T> {
        val entityTypeName = entityClass.simpleName.lowercase()
        val storageFile = File(databaseDirectory, "grug_$entityTypeName.ser")
        return loadCollection(entityClass, storageFile)
    }

    /**
     * Retrieves entities of a given type from the Grug database that match the specified condition.
     */
    @Synchronized
    fun <T : Any> find(
        entityClass: Class<T>,
        condition: (T) -> Boolean
    ): List<T> {
        val entityTypeName = entityClass.simpleName.lowercase()
        val storageFile = File(databaseDirectory, "grug_$entityTypeName.ser")
        return loadCollection(entityClass, storageFile).filter(condition)
    }

    /**
     * Deletes entities of a given type from the Grug database that match the specified condition.
     */
    @Synchronized
    fun <T : Any> delete(
        entityClass: Class<T>,
        condition: (T) -> Boolean
    ) {
        val entityTypeName = entityClass.simpleName.lowercase()
        val storageFile = File(databaseDirectory, "grug_$entityTypeName.ser")
        val entityList = loadCollection(entityClass, storageFile).toMutableList()

        val originalSize = entityList.size
        entityList.removeAll(condition)
        val updatedSize = entityList.size

        logger.debug("Deleted {} objects of type {}", originalSize - updatedSize, entityTypeName)

        if (entityList.isEmpty()) {
            if (!storageFile.delete()) {
                logger.debug("File couldn't be deleted")
            }
            logger.debug("Collection empty, deleted file {}", storageFile.name)
            return
        }

        FileOutputStream(storageFile).use { fileOutput ->
            ObjectOutputStream(fileOutput).use { objectOutput ->
                objectOutput.writeObject(entityList)
            }
        }
    }

    /**
     * Loads a collection of entities of a given type from the Grug database file.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> loadCollection(
        entityClass: Class<T>,
        storageFile: File
    ): List<T> {
        val entityTypeName = entityClass.simpleName.lowercase()
        if (!storageFile.exists() || storageFile.length() == 0L) {
            logger.debug("No data found for {}, returning empty list", entityTypeName)
            return emptyList()
        }

        return try {
            FileInputStream(storageFile).use { fileInput ->
                ObjectInputStream(fileInput).use { objectInput ->
                    val deserializedObject = objectInput.readObject()
                    if (deserializedObject is List<*>) {
                        deserializedObject as List<T>
                    } else {
                        logger.warn("Corrupted data for {}, resetting to empty list", entityTypeName)
                        emptyList()
                    }
                }
            }
        } catch (e: Exception) {
            when (e) {
                is ClassNotFoundException,
                is IOException -> {
                    logger.error("Error loading {}: {}", entityTypeName, e.message)
                    throw IOException("Failed to deserialize collection", e)
                }
                else -> throw e
            }
        }
    }
}