package com.example.infone.fetchers

import org.springframework.stereotype.Component

@Component
class SP500 : StockIndexFetcher("^GSPC", "S&P 500", "S&P 500 index at close")

@Component
class STOXX50E : StockIndexFetcher("^STOXX50E", "STOXX 50 Europe", "STOXX 50 Europe index at close")