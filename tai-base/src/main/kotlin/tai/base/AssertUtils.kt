package tai.base

import tai.base.ex.AssertionException

fun assertThat(status: Boolean, lazyMsg: () -> String) {
    if (!status) throw AssertionException(lazyMsg());
}