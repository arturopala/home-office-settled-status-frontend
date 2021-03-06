/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.homeofficesettledstatus.controllers

import scala.concurrent.Future

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.homeofficesettledstatus.views.LayoutComponents
import uk.gov.hmrc.homeofficesettledstatus.views.html.AccessibilityStatementPage
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

@Singleton
class AccessibilityStatementController @Inject()(
  controllerComponents: MessagesControllerComponents,
  layoutComponents: LayoutComponents)(implicit configuration: Configuration)
    extends FrontendController(controllerComponents) {

  val accessibilityStatementPage = new AccessibilityStatementPage(layoutComponents)

  val showPage: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(accessibilityStatementPage()))
  }
}
