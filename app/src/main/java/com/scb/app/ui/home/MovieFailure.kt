package com.scb.app.ui.home

import com.scb.app.exception.Failure.FeatureFailure


class MovieFailure {
    class ListNotAvailable :  FeatureFailure()
    class NonExistentMovie : FeatureFailure()
}

