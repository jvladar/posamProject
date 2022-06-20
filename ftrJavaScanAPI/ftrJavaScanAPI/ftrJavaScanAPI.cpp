//ftrJavaScanAPI.cpp : Defines the entry point for the DLL application.
//
#ifdef _WINDOWS
#pragma warning (disable:4996)
#endif

#include "ftrScanAPI.h"
#include "ftrJavaScanAPI.h"

#ifdef FTR_OS_UNIX
#include <string.h>
#endif

FTRHANDLE hDevice = NULL;
FTRSCAN_IMAGE_SIZE m_ImageSize;
FTR_DWORD m_dwErrCode = 0;

#ifdef _WINDOWS
BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
					 )
{
    return TRUE;
}
#endif

JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_OpenDevice(JNIEnv *env, jobject obj)
{
	hDevice = ftrScanOpenDevice();
	if( hDevice == NULL )
		return JNI_FALSE;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_CloseDevice(JNIEnv *env, jobject obj)
{
	if( hDevice != NULL )
	{
		ftrScanCloseDevice(hDevice);
		hDevice = NULL;
	}
	return JNI_TRUE;
}

JNIEXPORT jstring JNICALL Java_com_Futronic_ScanApiHelper_Scanner_GetVersionInfo(JNIEnv *env, jobject obj)
{
	if( hDevice == NULL )
		return NULL;	    	

	char szInfo[128];
	memset(szInfo, 0, 128);
	FTRSCAN_DEVICE_INFO infoDevice;
	// Initialize the FTRSCAN_DEVICE_INFO structure.
	infoDevice.dwStructSize = sizeof(infoDevice);
	FTR_BOOL bRet = ftrScanGetDeviceInfo( hDevice, &infoDevice );
	if( bRet )
	{
		switch( infoDevice.byDeviceCompatibility )
		{
		case FTR_DEVICE_USB_1_1:
		case FTR_DEVICE_USB_2_0_TYPE_1:
		case FTR_DEVICE_USB_2_0_TYPE_2:
		case FTR_DEVICE_USB_2_0_TYPE_80W:
			sprintf(szInfo, "%d:FS80", infoDevice.byDeviceCompatibility );
			break;
		case FTR_DEVICE_USB_2_0_TYPE_3:
			sprintf(szInfo, "%d:FS88", infoDevice.byDeviceCompatibility );
			break;
		case FTR_DEVICE_USB_2_0_TYPE_4:
		case FTR_DEVICE_USB_2_0_TYPE_90B:
			sprintf(szInfo, "%d:FS90", infoDevice.byDeviceCompatibility );
			break;
		case FTR_DEVICE_USB_2_0_TYPE_50:
			sprintf(szInfo, "%d:FS50", infoDevice.byDeviceCompatibility );
			break;
		case FTR_DEVICE_USB_2_0_TYPE_60:
			sprintf(szInfo, "%d:FS60", infoDevice.byDeviceCompatibility );
			break;
		case FTR_DEVICE_USB_2_0_TYPE_25:
			sprintf(szInfo, "%d:FS25", infoDevice.byDeviceCompatibility );
			break;
		default:
			sprintf(szInfo, "%d: -UNKNOWN Device-", infoDevice.byDeviceCompatibility );
			break;
		}
		//
		FTRSCAN_VERSION_INFO infoVersion;
		infoVersion.dwVersionInfoSize = sizeof( FTRSCAN_VERSION_INFO );
		bRet = ftrScanGetVersion( hDevice, &infoVersion );
		if( bRet )
		{
			char szVer[64];
			char szTmp[64];
			memset(szVer, 0, 64);
			memset(szTmp, 0, 64);
			sprintf(szVer, ",ScanAPI: %d.%d.%d.%d\r\n", 
				infoVersion.APIVersion.wMajorVersionHi, 
				infoVersion.APIVersion.wMajorVersionLo,
				infoVersion.APIVersion.wMinorVersionHi,
				infoVersion.APIVersion.wMinorVersionLo );
			strcat(szInfo, szVer);
			memset(szVer, 0, 64);
			sprintf(szVer, "Firmware: %d.%d", 
				infoVersion.FirmwareVersion.wMajorVersionHi,
				infoVersion.FirmwareVersion.wMajorVersionLo );
			if( infoVersion.FirmwareVersion.wMinorVersionHi != 0xffff )
			{
				sprintf(szTmp, ".%d", infoVersion.FirmwareVersion.wMinorVersionHi);
				strcat(szVer, szTmp);
				memset(szTmp, 0, 64);
			}
			if( infoVersion.FirmwareVersion.wMinorVersionLo != 0xffff )
			{
				sprintf( szTmp, ".%d", infoVersion.FirmwareVersion.wMinorVersionLo);
				strcat(szVer, szTmp);
				memset(szTmp, 0, 64);
			}
			sprintf(szTmp, ", Hardware: %d.%d", 
				infoVersion.HardwareVersion.wMajorVersionHi,
				infoVersion.HardwareVersion.wMajorVersionLo );
			strcat(szVer, szTmp);
			if( infoVersion.HardwareVersion.wMinorVersionHi != 0xffff )
			{
				sprintf(szTmp, ".%d", infoVersion.HardwareVersion.wMinorVersionHi);
				strcat(szVer, szTmp);
				memset(szTmp, 0, 64);
			}
			if( infoVersion.HardwareVersion.wMinorVersionLo != 0xffff )
			{
				sprintf( szTmp, ".%d", infoVersion.HardwareVersion.wMinorVersionLo);
				strcat(szVer, szTmp);
			}
			strcat(szInfo, szVer);
		}
		else
		{
			m_dwErrCode = ftrScanGetLastError();
			return NULL;
		}
	}
	else
	{
		m_dwErrCode = ftrScanGetLastError();
		return NULL;
	}
	
	return env->NewStringUTF((char *)szInfo);
}

JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_GetImageSize(JNIEnv *env, jobject obj)
{
	if( hDevice == NULL )
		return JNI_FALSE;	    	

    if( !ftrScanGetImageSize( hDevice, &m_ImageSize ) )
    {
		m_dwErrCode = ftrScanGetLastError();
		return JNI_FALSE;
    }

	/* Get a reference to obj¡¦s class */
	jclass cls = env->GetObjectClass(obj);
	jfieldID fidw = NULL; 
	jfieldID fidh = NULL;
    fidw = env->GetFieldID( cls, "m_ImageWidth", "I" );
    if( fidw == NULL )
        return JNI_FALSE;
	env->SetIntField( obj, fidw, m_ImageSize.nWidth );
	//
    fidh = env->GetFieldID( cls, "m_ImageHeight", "I" );
    if( fidh == NULL )
        return JNI_FALSE;
	env->SetIntField( obj, fidh, m_ImageSize.nHeight );
	
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_IsFingerPresent(JNIEnv *env, jobject obj)
{
	if( hDevice == NULL )
		return JNI_FALSE;

	if( ftrScanIsFingerPresent(hDevice, NULL) )
	{
		m_dwErrCode = ftrScanGetLastError();
		return JNI_TRUE;
	}
	return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_GetFrame(JNIEnv *env, jobject obj, jbyteArray pImage)
{
	if( hDevice == NULL )
		return JNI_FALSE;

	if( env->GetArrayLength(pImage) < m_ImageSize.nImageSize )
	{
		m_dwErrCode = FTR_ERROR_NOT_ENOUGH_MEMORY;
		return JNI_FALSE;
	}

    jboolean isCopy;
    jbyte *pJData = env->GetByteArrayElements( pImage, &isCopy );
	if( !ftrScanGetFrame( hDevice, pJData, NULL ) )
	{
		m_dwErrCode = ftrScanGetLastError();
		env->ReleaseByteArrayElements( pImage, pJData, 0 );
		return JNI_FALSE;
	}
	env->ReleaseByteArrayElements( pImage, pJData, 0 );
	
	//jfieldID fid; /* store the field ID */
	///* Get a reference to obj¡¦s class */
	//jclass cls = env->GetObjectClass(obj);
	///* Look for the instance field s in cls */
	//fid = env->GetFieldID(cls, "m_pImage",	"[B");
	//if (fid == NULL) 
	//	return JNI_FALSE;/* failed to find the field */
 //   jbyteArray pImage;

	//pImage = env->NewByteArray( (jsize)(m_ImageSize.nImageSize) );
 //   if( pImage == NULL )
 //   {
 //       return JNI_FALSE;
 //   }

 //   jboolean isCopy;
 //   jbyte *pJData = env->GetByteArrayElements( pImage, &isCopy );
 //   memcpy( pJData, m_pImage, m_ImageSize.nImageSize );
 //   env->ReleaseByteArrayElements( pImage, pJData, 0 );

 //   env->SetObjectField( obj, fid, pImage );

	return JNI_TRUE;
}


JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_GetImage2(JNIEnv *env, jobject obj, jint dose, jbyteArray pImage)
{
	if( hDevice == NULL )
		return JNI_FALSE;

	if( env->GetArrayLength(pImage) < m_ImageSize.nImageSize )
	{
		m_dwErrCode = FTR_ERROR_NOT_ENOUGH_MEMORY;
		return JNI_FALSE;
	}

	int nDose = dose;
    jboolean isCopy;
    jbyte *pJData = env->GetByteArrayElements( pImage, &isCopy );

	if( !ftrScanGetImage2( hDevice, nDose, pJData) )
	{
		m_dwErrCode = ftrScanGetLastError();
		env->ReleaseByteArrayElements( pImage, pJData, 0 );
		return JNI_FALSE;
	}
	env->ReleaseByteArrayElements( pImage, pJData, 0 );

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_SetOptions(JNIEnv *env, jobject obj, jint mask, jint flag)
{
	if( hDevice == NULL )
		return JNI_FALSE;

	unsigned long ulMask = (unsigned long)mask;
	unsigned long ulFlags = (unsigned long)flag;

	if( !ftrScanSetOptions(hDevice, ulMask, ulFlags ) )
	{
		m_dwErrCode = ftrScanGetLastError();
		return JNI_FALSE;
	}
	return JNI_TRUE;
}

JNIEXPORT jint JNICALL Java_com_Futronic_ScanApiHelper_Scanner_GetLastErrorCode(JNIEnv *env, jobject obj)
{
	return (jint)m_dwErrCode;
}

JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_Save7Bytes(JNIEnv *env, jobject obj, jbyteArray buffer)
{
	if( hDevice == NULL )
		return JNI_FALSE;

	if( env->GetArrayLength(buffer) != 7 )
	{
		m_dwErrCode = FTR_ERROR_INVALID_PARAMETER;
		return JNI_FALSE;
	}

    jboolean isCopy;
    jbyte *pJData = env->GetByteArrayElements( buffer, &isCopy );

	if( !ftrScanSave7Bytes( hDevice, pJData ) )
	{
		m_dwErrCode = ftrScanGetLastError();
	    env->ReleaseByteArrayElements( buffer, pJData, 0 );
		return JNI_FALSE;
	}
    env->ReleaseByteArrayElements( buffer, pJData, 0 );
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_Restore7Bytes(JNIEnv *env, jobject obj, jbyteArray buffer)
{
	if( hDevice == NULL )
		return JNI_FALSE;

	if( env->GetArrayLength(buffer) != 7 )
	{
		m_dwErrCode = FTR_ERROR_INVALID_PARAMETER;
		return JNI_FALSE;
	}

	char buf7Bytes[7];
	memset( buf7Bytes, 0, 7 );	
	if( !ftrScanRestore7Bytes( hDevice, buf7Bytes ) )
	{
		m_dwErrCode = ftrScanGetLastError();
		return JNI_FALSE;
	}
    jboolean isCopy;
    jbyte *pJData = env->GetByteArrayElements( buffer, &isCopy );
    memcpy( pJData, buf7Bytes, 7 );
    env->ReleaseByteArrayElements( buffer, pJData, 0 );

	return JNI_TRUE;
}


JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_SetNewAuthorizationCode(JNIEnv *env, jobject obj, jbyteArray SevenBytesAuthorizationCode)
{
	if( hDevice == NULL )
		return JNI_FALSE;

	if( env->GetArrayLength(SevenBytesAuthorizationCode) != 7 )
	{
		m_dwErrCode = FTR_ERROR_INVALID_PARAMETER;
		return JNI_FALSE;
	}
    jboolean isCopy;
    jbyte *pJData = env->GetByteArrayElements( SevenBytesAuthorizationCode, &isCopy );

	if( !ftrScanSetNewAuthorizationCode( hDevice, pJData ) )
	{
		m_dwErrCode = ftrScanGetLastError();
	    env->ReleaseByteArrayElements( SevenBytesAuthorizationCode, pJData, 0 );
		return JNI_FALSE;
	}
    env->ReleaseByteArrayElements( SevenBytesAuthorizationCode, pJData, 0 );
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_SaveSecret7Bytes(JNIEnv *env, jobject obj, jbyteArray SevenBytesAuthorizationCode, jbyteArray buffer)
{
	if( hDevice == NULL )
		return JNI_FALSE;

	if( env->GetArrayLength(SevenBytesAuthorizationCode) != 7 || 
		env->GetArrayLength(buffer) != 7 )
	{
		m_dwErrCode = FTR_ERROR_INVALID_PARAMETER;
		return JNI_FALSE;
	}

    jboolean isCopy;
    jbyte *pJData1 = env->GetByteArrayElements( SevenBytesAuthorizationCode, &isCopy );
    jbyte *pJData2 = env->GetByteArrayElements( buffer, &isCopy );

	if( !ftrScanSaveSecret7Bytes( hDevice, pJData1, pJData2 ) )
	{
		m_dwErrCode = ftrScanGetLastError();
	    env->ReleaseByteArrayElements( SevenBytesAuthorizationCode, pJData1, 0 );
	    env->ReleaseByteArrayElements( buffer, pJData2, 0 );
		return JNI_FALSE;
	}
    env->ReleaseByteArrayElements( SevenBytesAuthorizationCode, pJData1, 0 );
    env->ReleaseByteArrayElements( buffer, pJData2, 0 );
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_RestoreSecret7Bytes(JNIEnv *env, jobject obj, jbyteArray SevenBytesAuthorizationCode, jbyteArray buffer)
{
	if( hDevice == NULL )
		return JNI_FALSE;

	if( env->GetArrayLength(SevenBytesAuthorizationCode) != 7 || 
		env->GetArrayLength(buffer) != 7 )
	{
		m_dwErrCode = FTR_ERROR_INVALID_PARAMETER;
		return JNI_FALSE;
	}

    jboolean isCopy;
    jbyte *pJData1 = env->GetByteArrayElements( SevenBytesAuthorizationCode, &isCopy );
	char bufRead[7];
	memset( bufRead, 0, 7 );
	if( !ftrScanRestoreSecret7Bytes( hDevice, pJData1, bufRead ) )
	{
		m_dwErrCode = ftrScanGetLastError();
	    env->ReleaseByteArrayElements( SevenBytesAuthorizationCode, pJData1, 0 );
		return JNI_FALSE;
	}
    env->ReleaseByteArrayElements( SevenBytesAuthorizationCode, pJData1, 0 );
    jbyte *pJData2 = env->GetByteArrayElements( buffer, &isCopy );
    memcpy( pJData2, bufRead, 7 );
    env->ReleaseByteArrayElements( buffer, pJData2, 0 );
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_SetDiodesStatus(JNIEnv *env, jobject obj, jint nGreenDiodeStatus, jint nRedDiodeStatus)
{
	if( hDevice == NULL )
		return JNI_FALSE;

	FTR_BYTE byGreen = (FTR_BYTE)nGreenDiodeStatus;
	FTR_BYTE byRed = (FTR_BYTE)nRedDiodeStatus;
	if( !ftrScanSetDiodesStatus( hDevice, byGreen, byRed ) )
	{
		m_dwErrCode = ftrScanGetLastError();
		return JNI_FALSE;
	}
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_Futronic_ScanApiHelper_Scanner_GetDiodesStatus(JNIEnv *env, jobject obj, jbyteArray Status)
{
	if( hDevice == NULL )
		return JNI_FALSE;

	if( env->GetArrayLength(Status) != 2 )
	{
		m_dwErrCode = FTR_ERROR_INVALID_PARAMETER;
		return JNI_FALSE;
	}

	FTR_BOOL bGreen = 0;
	FTR_BOOL bRed = 0;
	if( !ftrScanGetDiodesStatus( hDevice, &bGreen, &bRed ) )
	{
		m_dwErrCode = ftrScanGetLastError();
		return JNI_FALSE;
	}
	char buf[2];
	memset( buf, 0, 2 );
	if( bGreen )
		buf[0] = 1;
	if( bRed )
		buf[1] = 1;
    jboolean isCopy;
    jbyte *pJData = env->GetByteArrayElements( Status, &isCopy );
    memcpy( pJData, buf, 2 );
    env->ReleaseByteArrayElements( Status, pJData, 0 );
	return JNI_TRUE;
}
