package uk.gov.hmrc.homeofficesettledstatus.controllers

import play.api.mvc.Result
import play.api.mvc.Results._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.{Configuration, Environment}
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.homeofficesettledstatus.support.BaseISpec
import uk.gov.hmrc.http.{HeaderCarrier, SessionKeys}

import scala.concurrent.Future

class AuthActionsISpec extends BaseISpec {

  object TestController extends AuthActions {

    override def authConnector: AuthConnector = app.injector.instanceOf[AuthConnector]
    override def config: Configuration = app.injector.instanceOf[Configuration]
    override def env: Environment = app.injector.instanceOf[Environment]

    implicit val hc: HeaderCarrier = HeaderCarrier()
    implicit val request = FakeRequest().withSession(SessionKeys.authToken -> "Bearer XYZ")
    import scala.concurrent.ExecutionContext.Implicits.global

    def withAuthorisedWithStrideGroup[A](group: String): Result =
      await(super.authorisedWithStrideGroup(group) { pid =>
        Future.successful(Ok(pid))
      })

  }

  "withAuthorisedWithStrideGroup" should {

    "call body with a valid authProviderId" in {

      givenAuthorisedForStride("TBC", "StrideUserId")

      val result = TestController.withAuthorisedWithStrideGroup("TBC")

      status(result) shouldBe 200
      bodyOf(result) should include("StrideUserId")
    }

    "redirect to log in page when user not enrolled for the service" in {
      givenAuthorisedForStride("TBC", "StrideUserId")

      val result = TestController.withAuthorisedWithStrideGroup("OTHER")
      status(result) shouldBe 303
      redirectLocation(result).get should include("/stride/sign-in")
    }

    "redirect to log in page when user not authenticated" in {
      givenRequestIsNotAuthorised("SessionRecordNotFound")

      val result = TestController.withAuthorisedWithStrideGroup("TBC")
      status(result) shouldBe 303
      redirectLocation(result).get should include("/stride/sign-in")
    }

    "redirect to log in page when user authenticated with different provider" in {
      givenRequestIsNotAuthorised("UnsupportedAuthProvider")

      val result = TestController.withAuthorisedWithStrideGroup("TBC")
      status(result) shouldBe 303
      redirectLocation(result).get should include("/stride/sign-in")
    }
  }

}
