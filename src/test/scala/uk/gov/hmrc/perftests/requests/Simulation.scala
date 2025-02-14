/*
 * Copyright 2025 HM Revenue & Customs
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

package uk.gov.hmrc.perftests.requests

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.requests.ADDERRequst._
import uk.gov.hmrc.perftests.requests.STDERRequst._
import uk.gov.hmrc.perftests.requests.APDERRequst._
import uk.gov.hmrc.perftests.requests.Requests._

class Simulation extends PerformanceTestRunner {

  setup("Sdes Journey", "Navigate to sdes  page") withRequests (
    getLoginPage,
    postStrideAuthPage,
    postSamlResponse,
    getSdesLandingPage,
    getRequestDatamovement,
    getCreateDER,
    getCreateTitlename,
    postEnterDataMovementName,
    getHowDataMoved,
    postDigitaltransfer,
    getDirection,
    postDirection,
    getBusinessArea,
    postDERDirection,
    getTemplate,
    postInformationType,
    getmovingdata,
    postFrequency,
      getFrequency,
    postDailyFrequency,
      getRequestReviewed,
    postRequestSendingDate,
      getDataContentType,
      postContentType,
      getContentIdentify,
      postIndentify,
      getSpecialCustomer,
      postSpecialCustomer,
      getClassification,
      postClassification,
      getDataNeeded,
      postDataNeeded,
      getRequestors,
      postAddTeammember,
//    postAdditionalTeamMember,
      getOrganisationPage,
      postAddOrganisation,
      getIndentifiers,
      postIndentifiers,
      getAdditionalInfo,
      postAdditonalInformation,
      getCYAPage,
      postSubmitDER
  )

  setup("ADDer Journey", "Navigate to AD DER page") withRequests   (
    getLoginPage,
    postStrideAuthPage,
    postSamlResponse,
    getSdesLandingPage,
    getRequestDatamovement,
    getCreateDER,
    getCreateTitlename,
    postEnterDataMovementName,
    getHowDataMoved,
    postPhysicaltransfer,
    getSteps,
    postStepstomoveData,
    getDataProtected,
    postProtectedYes,
    getmovingdata,
    postFrequency,
    getFrequency,
    postPhysicalDailyFrequency,
    getStartdate,
    postStartDate,
    getRequestReviewed,
    postRequestSendingDate,
    getDataContentType,
    postContentType,
    getContentIdentify,
    postIndentify,
    getSpecialCustomer,
    postSpecialCustomer,
    getClassification,
    postSecurityClassification,
    getApprovalgroup,
    postApprovalgroup,
    getDataNeeded,
    postDataNeeded,
    getRequestors,
    postAddTeammember,
//         postAdditionalTeamMember,
    getDatafrom,
    postDataFrom,
    getDataTo,
    postDataTo,
    getAdditionalInfo,
    postPhysicalDERAdditonalInformation,
    getPhysicalCYAPage,
    postPhysicalDERSubmit,
  )
  setup("STDer Journey", "Navigate to AP DER page") withRequests   (
    getLoginPage,
    postStrideAuthPage,
    postSamlResponse,
    getSdesLandingPage,
    getRequestDatamovement,
    getCreateDER,
    getCreateTitlename,
    postEnterDataMovementName,
    getHowDataMoved,
    postAPMEDIAtransfer,
    getStorageMedia,
    postDataStored,
    getSecureTransport,
    postSecureTransport,
    getSecureCourier,
    postSecureCourier,
    getDataTransferred,
    postDataTransferred,
    getDataProtected,
    postAPProtectedYes,
    postPackageSection,
    getPacakgeType,
    postPackageType,
    getPacakgeSize,
    postPackageSize,
    getPacakges,
    postPackages,
    getmovingdata,
    postFrequency,
    getFrequency,
    postApprovalOnlyFrequency,
    getFirstTargetdate,
    postTargetDate,
    getReviewDate,
    postReviewDate,
    getDataContentType,
    postContentType,
    getContentIdentify,
    postIndentify,
    getSpecialCustomer,
    postSpecialCustomer,
    getClassification,
    postSecurityClassification,
    getApprovalgroup,
    postApprovalgroup,
    getDataNeeded,
    postDataNeeded,
    getRequestors,
    postAddTeammember,
    //         postAdditionalTeamMember,

    getSenderContact,
    postSenderContactDetails,
    getAddAnotherSenderContact,
    postAnotherSenderContact,
    getAnotherSenderContact,
    postSecondSenderContactDetails,
    getSecondSenderContact,
//    postContact,
    getSourceAddress,
    getSourceAddress,
    postSourceAddress,
    getrecipientcontactdetails,
    postRecipientContactDetails,
    getAddAnotherRecipientContactDetails,
    postAnotherRecepientContact,
    getNewRecipientContactDetails,
    postAnotherRecipientContactDetails,
    getRecipientContactDetails,
    postNoRecipientContactDetails,
    getDestinationAddress,
    postDestinationAddress,
    getAdditionalInfo,
    postSTDERAdditonalInformation,
    getSTDERCYAPage,
    postStoragemMediaAnnswersSubmit,
  )
  setup("APDer Journey", "Navigate to AP DER page") withRequests   (
    getLoginPage,
    postStrideAuthPage,
    postSamlResponse,
    getSdesLandingPage,
    getRequestDatamovement,
    getCreateDER,
    getCreateTitlename,
    postEnterDataMovementName,
    getHowDataMoved,
    postAPMEDIAtransfer,
    getStorageMedia,
    postDataStored,
    getSecureTransport,
    postNotASecureTransport,
    getMoveStorageMedia,
    postStorageMedia,
    getDataProtected,
    postAPProtectedYes,
    getPackaging,
    postAPPackaging,
    getPackagingDetails,
    postPackagingDetails,
    getmovingdata,
    postFrequency,
    getFrequency,
    postPhysicalDailyFrequency,
    getAPStartdate,
    postAPStartDate,
    getReviewDate,
    postReviewDate,
    getDataContentType,
    postContentType,
    getContentIdentify,
    postIndentify,
    getSpecialCustomer,
    postSpecialCustomer,
    getClassification,
    postSecurityClassification,
    getApprovalgroup,
    postApprovalgroup,
    getDataNeeded,
    postDataNeeded,
    getRequestors,
    postAddTeammember,
    //         postAdditionalTeamMember,
    getSenderorReceive,
    postSendorReceive,
    getrecipientcontactdetailsknown,
    postRecipientYes,
    getrecipientcontactdetailst,
    postAPRecipientContactDetails,
    getAdditionalInfo,
    postAPDERAdditonalInformation,
    getAPDERCYAPage,
    postStoragemMediaAnnswersSubmit,
  )
  runSimulation()
}
