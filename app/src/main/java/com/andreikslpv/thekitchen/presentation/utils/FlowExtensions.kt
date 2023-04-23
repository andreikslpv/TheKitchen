package com.andreikslpv.thekitchen.presentation.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

// CustomFlowOperator
/* Emits the previous values ('null' if there is no previous values) along with the current one.
For example:
- original flow:
"a", "b", "c" ...
- resulting flow (count = 2):
(null, null), (null, "a"), ("a", "b"), ("b", "c"), ...
 */
fun <T> Flow<T>.simpleScan(count: Int): Flow<List<T?>> {
    val items = List<T?>(count) { null }
    return this.scan(items) { previous, value -> previous.drop(1) + value }
}