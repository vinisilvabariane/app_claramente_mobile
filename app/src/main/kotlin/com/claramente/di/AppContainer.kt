package com.claramente.di

import com.claramente.BuildConfig
import com.claramente.core.auth.controller.SessionController
import com.claramente.core.data.contract.IDocumentReader
import com.claramente.core.data.contract.IModelFileStore
import com.claramente.core.data.contract.ITokenStore
import com.claramente.core.data.store.ContentDocumentReader
import com.claramente.core.data.store.DiskModelFileStore
import com.claramente.core.data.store.PrefsTokenStore
import com.claramente.core.domain.contract.ICreateClassroomUseCase
import com.claramente.core.domain.contract.IDeleteClassroomUseCase
import com.claramente.core.domain.contract.IDeleteModelUseCase
import com.claramente.core.domain.contract.IInspectModelFileUseCase
import com.claramente.core.domain.contract.IJoinClassroomUseCase
import com.claramente.core.domain.contract.ILeaveClassroomUseCase
import com.claramente.core.domain.contract.IListClassroomModelsUseCase
import com.claramente.core.domain.contract.IListClassroomsUseCase
import com.claramente.core.domain.contract.IListLibraryUseCase
import com.claramente.core.domain.contract.IListStudentsUseCase
import com.claramente.core.domain.contract.ILoginUseCase
import com.claramente.core.domain.contract.ILogoutUseCase
import com.claramente.core.domain.contract.IMockLoginUseCase
import com.claramente.core.domain.contract.IPrepareModelUseCase
import com.claramente.core.domain.contract.IRegenerateJoinCodeUseCase
import com.claramente.core.domain.contract.IRemoveStudentUseCase
import com.claramente.core.domain.contract.IRestoreSessionUseCase
import com.claramente.core.domain.contract.ISetModelPublishedUseCase
import com.claramente.core.domain.contract.IUploadModelUseCase
import com.claramente.core.domain.usecase.CreateClassroomUseCase
import com.claramente.core.domain.usecase.DeleteClassroomUseCase
import com.claramente.core.domain.usecase.DeleteModelUseCase
import com.claramente.core.domain.usecase.InspectModelFileUseCase
import com.claramente.core.domain.usecase.JoinClassroomUseCase
import com.claramente.core.domain.usecase.LeaveClassroomUseCase
import com.claramente.core.domain.usecase.ListClassroomModelsUseCase
import com.claramente.core.domain.usecase.ListClassroomsUseCase
import com.claramente.core.domain.usecase.ListLibraryUseCase
import com.claramente.core.domain.usecase.ListStudentsUseCase
import com.claramente.core.domain.usecase.LoginUseCase
import com.claramente.core.domain.usecase.LogoutUseCase
import com.claramente.core.domain.usecase.MockLoginUseCase
import com.claramente.core.domain.usecase.PrepareModelUseCase
import com.claramente.core.domain.usecase.RegenerateJoinCodeUseCase
import com.claramente.core.domain.usecase.RemoveStudentUseCase
import com.claramente.core.domain.usecase.RestoreSessionUseCase
import com.claramente.core.domain.usecase.SetModelPublishedUseCase
import com.claramente.core.domain.usecase.UploadModelUseCase
import com.claramente.core.network.client.HttpAuthClient
import com.claramente.core.network.client.HttpClassroomClient
import com.claramente.core.network.client.HttpModelClient
import com.claramente.core.network.contract.IAuthClient
import com.claramente.core.network.contract.IClassroomClient
import com.claramente.core.network.contract.IModelClient
import com.claramente.core.network.http.ClaramenteHttp
import android.content.Context

class AppContainer(val context: Context) {
    val tokenStore: ITokenStore by lazy { PrefsTokenStore(context) }

    val sessions: SessionController by lazy { SessionController() }

    private val http: ClaramenteHttp by lazy {
        ClaramenteHttp(
            BuildConfig.API_BASE_URL,
            onRefreshed = { tokenStore.save(it.snapshot()) },
            onRejected = {
                tokenStore.clear()
                sessions.clear()
            },
        )
    }

    val authClient: IAuthClient by lazy { HttpAuthClient(http) }

    private val classroomClient: IClassroomClient by lazy { HttpClassroomClient(http) }

    private val modelClient: IModelClient by lazy { HttpModelClient(http) }

    private val modelFiles: IModelFileStore by lazy { DiskModelFileStore(context) }

    private val documents: IDocumentReader by lazy { ContentDocumentReader(context) }

    val restoreSession: IRestoreSessionUseCase by lazy { RestoreSessionUseCase(authClient, tokenStore, sessions) }

    val login: ILoginUseCase by lazy { LoginUseCase(authClient, tokenStore, sessions) }

    val mockLogin: IMockLoginUseCase by lazy { MockLoginUseCase(tokenStore, sessions) }

    val logout: ILogoutUseCase by lazy { LogoutUseCase(authClient, tokenStore, sessions) }

    val listClassrooms: IListClassroomsUseCase by lazy { ListClassroomsUseCase(classroomClient, sessions) }

    val joinClassroom: IJoinClassroomUseCase by lazy { JoinClassroomUseCase(classroomClient, sessions) }

    val leaveClassroom: ILeaveClassroomUseCase by lazy { LeaveClassroomUseCase(classroomClient, sessions) }

    val listClassroomModels: IListClassroomModelsUseCase by lazy { ListClassroomModelsUseCase(classroomClient, sessions) }

    val prepareModel: IPrepareModelUseCase by lazy { PrepareModelUseCase(modelClient, modelFiles, sessions) }

    val createClassroom: ICreateClassroomUseCase by lazy { CreateClassroomUseCase(classroomClient, sessions) }

    val deleteClassroom: IDeleteClassroomUseCase by lazy { DeleteClassroomUseCase(classroomClient, sessions) }

    val regenerateJoinCode: IRegenerateJoinCodeUseCase by lazy { RegenerateJoinCodeUseCase(classroomClient, sessions) }

    val listStudents: IListStudentsUseCase by lazy { ListStudentsUseCase(classroomClient, sessions) }

    val removeStudent: IRemoveStudentUseCase by lazy { RemoveStudentUseCase(classroomClient, sessions) }

    val listLibrary: IListLibraryUseCase by lazy { ListLibraryUseCase(modelClient, sessions) }

    val inspectModelFile: IInspectModelFileUseCase by lazy { InspectModelFileUseCase(documents) }

    val uploadModel: IUploadModelUseCase by lazy { UploadModelUseCase(modelClient, documents, sessions) }

    val deleteModel: IDeleteModelUseCase by lazy { DeleteModelUseCase(modelClient, modelFiles, sessions) }

    val setModelPublished: ISetModelPublishedUseCase by lazy { SetModelPublishedUseCase(classroomClient, sessions) }

    val mockAuthEnabled: Boolean = BuildConfig.MOCK_AUTH

    val arWebUrl: String? = BuildConfig.AR_WEB_URL.takeIf { it.isNotBlank() }
}
