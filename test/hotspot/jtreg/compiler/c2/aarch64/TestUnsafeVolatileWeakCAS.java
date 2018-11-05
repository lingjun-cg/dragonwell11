/*
 * Copyright (c) 2018, Red Hat, Inc. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package compiler.c2.aarch64;

import java.lang.reflect.Field;
import jdk.internal.misc.Unsafe;

class TestUnsafeVolatileWeakCAS
{
    public volatile int f_int = 0;
    public volatile Integer f_obj = Integer.valueOf(0);
    public volatile long f_long = 0;
    public volatile byte f_byte = 0;
    public volatile short f_short = 0;

    public static Unsafe unsafe = Unsafe.getUnsafe();
    public static Field f_int_field;
    public static Field f_obj_field;
    public static Field f_long_field;
    public static Field f_byte_field;
    public static Field f_short_field;
    public static long f_int_off;
    public static long f_obj_off;
    public static long f_long_off;
    public static long f_byte_off;
    public static long f_short_off;

    static {
        try {
            f_int_field = TestUnsafeVolatileWeakCAS.class.getField("f_int");
            f_obj_field = TestUnsafeVolatileWeakCAS.class.getField("f_obj");
            f_long_field = TestUnsafeVolatileWeakCAS.class.getField("f_long");
            f_byte_field = TestUnsafeVolatileWeakCAS.class.getField("f_byte");
            f_short_field = TestUnsafeVolatileWeakCAS.class.getField("f_short");
            f_int_off = unsafe.objectFieldOffset(f_int_field);
            f_obj_off = unsafe.objectFieldOffset(f_obj_field);
            f_long_off = unsafe.objectFieldOffset(f_long_field);
            f_byte_off = unsafe.objectFieldOffset(f_byte_field);
            f_short_off = unsafe.objectFieldOffset(f_short_field);
        } catch (Exception e) {
            System.out.println("reflection failed " + e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        final TestUnsafeVolatileWeakCAS t = new TestUnsafeVolatileWeakCAS();
        for (int i = 0; i < 100_000; i++) {
            t.f_int = -1;
            if (t.testInt(-1, i)) {
                if (t.f_int != i) {
                    throw new RuntimeException("bad result!");
                }
            }
        }
        for (int i = 0; i < 100_000; i++) {
            t.f_long = -1;
            if (t.testLong(-1, i)) {
                if (t.f_long != i) {
                    throw new RuntimeException("bad result!");
                }
            }
        }
        for (int i = 0; i < 100_000; i++) {
            t.f_byte = -1;
            byte i_b = (byte)i;
            if (t.testByte((byte)-1, i_b)) {
                if (t.f_byte != i_b) {
                    throw new RuntimeException("bad result!");
                }
            }
        }
        for (int i = 0; i < 100_000; i++) {
            t.f_short = -1;
            short i_s = (short)i;
            if (t.testShort((byte)-1, i_s)) {
                if (t.f_short != i_s) {
                    throw new RuntimeException("bad result!");
                }
            }
        }
        Integer minusOne = Integer.valueOf(-1);
        for (int i = 0; i < 100_000; i++) {
            t.f_obj = minusOne;
            if (t.testObj(minusOne, Integer.valueOf(i))) {
                if (t.f_obj != i) {
                    throw new RuntimeException("bad result!");
                }
            }
        }
    }

    public boolean testInt(int x, int i)
    {
        return unsafe.weakCompareAndSetInt(this, f_int_off, x, i);
    }

    public boolean testObj(Object x, Object o)
    {
        return unsafe.weakCompareAndSetObject(this, f_obj_off, x, o);
    }

    public boolean testLong(long x, long i)
    {
        return unsafe.weakCompareAndSetLong(this, f_long_off, x, i);
    }

    public boolean testByte(byte x, byte i)
    {
        return unsafe.weakCompareAndSetByte(this, f_byte_off, x, i);
    }

    public boolean testShort(short x, short i)
    {
        return unsafe.weakCompareAndSetShort(this, f_short_off, x, i);
    }
}
