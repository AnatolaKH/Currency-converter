package akh.core.di

import akh.core.di.data.RateRepositoryToolsProvider
import akh.core.di.data.SecretDBToolsProvider

interface DataToolsProvider :
    RateRepositoryToolsProvider,
    SecretDBToolsProvider