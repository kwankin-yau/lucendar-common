/*******************************************************************************
 *  Copyright (c) 2019, 2022 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
package com.lucendar.common.types;

public enum OsType {
    LinuxOrUnix,
    Windows,
    MacOs,
    Solaris,
    Others;


    public static String osName() {
        return System.getProperty("os.name");
    }

    public static String osNameLowerCase() {
        return osName().toLowerCase();
    }

    public static OsType of(String osName) {
        if (osName == null)
            throw new IllegalArgumentException("`osName` is null");

        String osNameLC = osName.toLowerCase();

        if (osNameLC.contains("lin") || osNameLC.contains("nix"))
            return OsType.LinuxOrUnix;
        else if (osNameLC.contains("win"))
            return OsType.Windows;
        else if (osNameLC.contains("mac"))
            return OsType.MacOs;
        else if (osNameLC.contains("sunos"))
            return OsType.Solaris;
        else {
            return OsType.Others;
        }
    }

    public static OsType get() {
        return of(osName());
    }

    public static boolean osIsWindows() {
        return get() == Windows;
    }

    public static boolean osIsLinuxOrUnix() {
        return get() == LinuxOrUnix;
    }

    public static boolean osIsMacOs() {
        return get() == MacOs;
    }

    public static boolean osIsSolaris() {
        return get() == Solaris;
    }

    public boolean isWindows() {
        return this == Windows;
    }

    public boolean isLinuxOrUnix() {
        return this == LinuxOrUnix;
    }

    public boolean isMacOs() {
        return this == MacOs;
    }

    public boolean isSolaris() {
        return this == Solaris;
    }


}
