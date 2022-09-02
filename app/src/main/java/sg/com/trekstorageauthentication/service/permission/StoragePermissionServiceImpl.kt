package sg.com.trekstorageauthentication.service.permission

import android.Manifest
import android.os.Build

class StoragePermissionServiceImpl : PermissionService {
    override fun getRequiredPermissions(): List<String> {
        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
        }

        return permissions.toList()
    }

    override fun getPermissionResult(permissionResults: Map<String, Boolean>): Boolean {
        var isPermissionGranted = true
        kotlin.run loop@{
            permissionResults.forEach { (_, result) ->
                if (!result) {
                    isPermissionGranted = false
                    return@loop
                }
            }
        }

        return isPermissionGranted
    }
}