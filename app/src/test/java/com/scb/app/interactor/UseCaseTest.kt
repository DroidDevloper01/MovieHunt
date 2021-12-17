package com.scb.app.interactor

import com.scb.app.AndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import com.scb.app.functional.Either.Left
import com.scb.app.functional.Either.Right
import com.scb.app.exception.Failure.ServerError
import org.amshove.kluent.shouldEqual

class UseCaseTest : AndroidTest() {

    private val useCase = MyUseCase()

    @Test fun `running use case should return 'Either' of use case type`() {
        val params = MyParams(TYPE_PARAM)
        val result = runBlocking { useCase.run(params) }

        result shouldEqual Right(MyType(TYPE_TEST))
    }


    data class MyType(val name: String)
    data class MyParams(val name: String)

    private inner class MyUseCase : UseCase<MyType, MyParams>() {
        override suspend fun run(params: MyParams) = Right(MyType(TYPE_TEST))
    }

    companion object {
        private const val TYPE_TEST = "Test"
        private const val TYPE_PARAM = "ParamTest"
    }
}
