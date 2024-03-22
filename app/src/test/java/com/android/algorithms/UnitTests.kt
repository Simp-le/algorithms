package com.android.algorithms

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    RepositoryTests::class,
    ViewModelTests::class,
    MapperTests::class
)
internal class AllTests
