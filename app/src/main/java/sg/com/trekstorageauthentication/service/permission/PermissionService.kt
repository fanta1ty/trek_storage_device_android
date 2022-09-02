package sg.com.trekstorageauthentication.service.permission

interface PermissionService {
    fun getRequiredPermissions(): List<String>

    fun getPermissionResult(permissionResults: Map<String, Boolean>): Boolean
}