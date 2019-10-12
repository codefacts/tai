package tai.base

import tai.base.ex.AssertionException
import java.lang.Exception

fun assertThat(status: Boolean, lazyMsg: () -> String) {
    if (!status) throw AssertionException(lazyMsg());
}

fun assertOrThrow(status: Boolean, lazyMsg: () -> Exception) {
    if (!status) throw lazyMsg();
}