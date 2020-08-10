package org.camunda.latera.bss.connectors

import org.camunda.latera.bss.HttpClient.HttpProcessor
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.*

data class Export(
             val id: String,
             val status: String)

data class ExecuteReportParams(
             val reportUnitUri: String,
             val parameters: Map<String, Any>,
             val outputFormat: String = "pdf",
             val freshData: Boolean = false,
             val saveDataSnapshot: Boolean = false,
             val interactive: Boolean = true,
             val allowInlineScripts: Boolean = true,
             val ignorePagination: Boolean? = null,
             val pages: Int? = null,
             val async: Boolean = false,
             val transformerKey: String? = null,
             val attachmentsPrefix: String = "attachments",
             val baseUrl: String? = null)

data class ExecuteReportResponse(
             val currentPage: Int,
             val exports: Array<Export>,
             val reportURI: String,
             val requestId: String,
             val status: String)

data class Options(
             val outputFormat: String,
             val attachmentsPrefix: String,
             val baseUrl: String? = null,
             val allowInlineScripts: Boolean)

data class OutputResource(
             val contentType: String)

data class Attachment(
             val contentType: String,
             val fileName: String)

data class DetailsExport(
             val id: String,
             val options: Options,
             val status: String,
             val outputResource: OutputResource,
             val attachments: Array<Attachment>)

data class ExecutionDetailsResponse(
             val status: String,
             val totalPages: Int,
             val requestId: String,
             val reportURI: String,
             val exports: Array<DetailsExport>)

data class ErrorDescriptor(
             val message: String,
             val errorCode: String,
             val parameters: Array<String>)

data class ExecutionStatusResponse(
             val value: String,
             val errorDescriptor: ErrorDescriptor? = null)

class JasperReport(val url: String, val user: String, val password: String) {
  private val baseUrl: java.net.URI = java.net.URI(this.url)

  fun executeReport(params: ExecuteReportParams): ExecuteReportResponse {
    val executeReportUrl: String = this.baseUrl.resolve("/reportExecutions").toString()

    val client = HttpProcessor.getJasperClient(this.user, this.password)
    val response = runBlocking {
      client.post<ExecuteReportResponse>(executeReportUrl){
        contentType(ContentType.Application.Json)
        body = params
      }
    }

    client.close()
    return response
  }

  fun getReportResult(requestId: String, exportId: String):  String {
    val reportResultUrl: String = this.baseUrl.resolve("/reportExecutions/${requestId}/exports/${exportId}/outputResource").toString()

    val client = HttpProcessor.getJasperClient(this.user, this.password)
    val response = runBlocking {
      client.get<String>(reportResultUrl){
        contentType(ContentType.Application.Json)
      }
    }

    client.close()
    return response
  }

  fun getFileFromReport(requestId: String, exportId: String, fileName: String, attachmentsPrefix: String = "attachments"): String {
    val fileFromReportUrl: String = this.baseUrl.resolve("/reportExecutions/${requestId}/exports/${exportId}/${attachmentsPrefix}/${fileName}").toString()

    val client = HttpProcessor.getJasperClient(this.user, this.password)
    val response = runBlocking {
      client.get<String>(fileFromReportUrl){
        contentType(ContentType.Application.Json)
      }
    }

    client.close()
    return response
  }

  fun getReportExecutionDetails(requestId: String): ExecutionDetailsResponse {
    val executionDetailsUrl: String = this.baseUrl.resolve("/reportExecutions/${requestId}").toString()

    val client = HttpProcessor.getJasperClient(this.user, this.password)
    val response = runBlocking {
      client.get<ExecutionDetailsResponse>(executionDetailsUrl){
        contentType(ContentType.Application.Json)
      }
    }

    client.close()
    return response
  }

  fun getReportExecutionStatus(requestId: String): ExecutionStatusResponse {
    val executionStatusUrl: String = this.baseUrl.resolve("/reportExecutions/${requestId}/status").toString()

    val client = HttpProcessor.getJasperClient(this.user, this.password)
    val response = runBlocking {
      client.get<ExecutionStatusResponse>(executionStatusUrl){
        contentType(ContentType.Application.Json)
      }
    }

    client.close()
    return response
  }
}
