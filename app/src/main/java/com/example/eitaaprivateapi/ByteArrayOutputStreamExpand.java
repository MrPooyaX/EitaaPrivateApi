package com.example.eitaaprivateapi;

import java.io.OutputStream;

/* loaded from: classes.dex */
public class ByteArrayOutputStreamExpand extends OutputStream {
    protected byte[] buf;
    protected int count;

    public ByteArrayOutputStreamExpand() {
        this.buf = new byte[32];
    }

    public ByteArrayOutputStreamExpand(int size) {
        if (size >= 0) {
            this.buf = new byte[size];
            return;
        }
        throw new IllegalArgumentException("size < 0");
    }

    private void expand(int i) {
        int i2 = this.count;
        int i3 = i2 + i;
        byte[] bArr = this.buf;
        if (i3 <= bArr.length) {
            return;
        }
        byte[] bArr2 = new byte[i + i2];
        System.arraycopy(bArr, 0, bArr2, 0, i2);
        this.buf = bArr2;
    }

    public synchronized void reset() {
        this.count = 0;
    }

    public int size() {
        return this.count;
    }

    public byte[] toByteArray() {
        return this.buf;
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }

    @Override // java.io.OutputStream
    public void write(byte[] buffer, int offset, int len) {
        checkOffsetAndCount(buffer.length, offset, len);
        if (len == 0) {
            return;
        }
        expand(len);
        System.arraycopy(buffer, offset, this.buf, this.count, len);
        this.count += len;
    }

    @Override // java.io.OutputStream
    public void write(int oneByte) {
        if (this.count == this.buf.length) {
            expand(1);
        }
        byte[] bArr = this.buf;
        int i = this.count;
        this.count = i + 1;
        bArr[i] = (byte) oneByte;
    }

    public void checkOffsetAndCount(int arrayLength, int offset, int count) {
        if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
            throw new ArrayIndexOutOfBoundsException("length=" + arrayLength + "; regionStart=" + offset + "; regionLength=" + count);
        }
    }
}