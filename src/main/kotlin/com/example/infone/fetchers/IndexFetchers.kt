package com.example.infone.fetchers

import org.springframework.stereotype.Component

@Component
class SP500 : IndexFetcher("^GSPC", "S&P 500", "S&P 500 index at close")

@Component
class STOXX50E : IndexFetcher("^STOXX50E", "STOXX 50 Europe", "STOXX 50 Europe index at close")

@Component
class DowJones : IndexFetcher("^DJI", "Dow Jones Indus. Avg", "Dow Jones Industrial Average at close")

@Component
class Nasdaq : IndexFetcher("^IXIC", "NASDAQ Composite", "NASDAQ Composite index at close")

@Component
class FTSE100 : IndexFetcher("^FTSE", "FTSE 100", "FTSE 100 index at close")

@Component
class Nikkei225 : IndexFetcher("^N225", "Nikkei 225", "Nikkei 225 index at close")