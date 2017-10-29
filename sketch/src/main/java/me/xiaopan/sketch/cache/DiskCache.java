/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.xiaopan.sketch.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;

import me.xiaopan.sketch.Identifier;
import me.xiaopan.sketch.util.DiskLruCache;

/**
 * 磁盘缓存管理器
 */
public interface DiskCache extends Identifier {
    String DISK_CACHE_DIR_NAME = "sketch";
    int DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024;
    int DISK_CACHE_RESERVED_SPACE_SIZE = 200 * 1024 * 1024;

    /**
     * 是否存在
     */
    boolean exist(@NonNull String uri);

    /**
     * 获取缓存实体
     */
    @Nullable
    Entry get(@NonNull String uri);

    /**
     * 编辑缓存
     */
    @Nullable
    Editor edit(@NonNull String uri);

    /**
     * 获取缓存目录
     */
    @NonNull
    @SuppressWarnings("unused")
    File getCacheDir();

    /**
     * 获取最大容量（默认为100M）
     */
    long getMaxSize();

    /**
     * 将uri地址进行转码作为缓存文件的名字
     */
    @NonNull
    String uriToDiskCacheKey(@NonNull String uri);

    /**
     * 获取已用容量
     */
    long getSize();

    /**
     * 禁用了？
     */
    @SuppressWarnings("unused")
    boolean isDisabled();

    /**
     * 设置禁用
     *
     * @param disabled 禁用
     */
    void setDisabled(boolean disabled);

    /**
     * 清除缓存
     */
    void clear();

    /**
     * 是否已关闭
     */
    @SuppressWarnings("unused")
    boolean isClosed();

    /**
     * 关闭
     */
    void close();

    /**
     * 获取编辑锁
     *
     * @param uri 下载uri
     * @return ReentrantLock
     */
    @NonNull
    ReentrantLock getEditLock(@NonNull String uri);

    /**
     * 磁盘缓存实体
     */
    interface Entry {
        /**
         * 创建输入流
         *
         * @return InputStream
         * @throws IOException
         */
        @NonNull
        InputStream newInputStream() throws IOException;

        /**
         * 获取实体文件
         *
         * @return File
         */
        @NonNull
        File getFile();

        /**
         * 获取实体对应的uri
         *
         * @return 对应的uri，未转码的
         */
        @NonNull
        String getUri();

        /**
         * 删除实体
         *
         * @return true：删除成功
         */
        boolean delete();
    }

    /**
     * 磁盘缓存编辑器
     */
    interface Editor {
        /**
         * 创建一个输出流，用于写出文件
         *
         * @return OutputStream
         * @throws IOException
         */
        OutputStream newOutputStream() throws IOException;

        /**
         * 写完提交
         *
         * @throws IOException
         * @throws DiskLruCache.EditorChangedException
         * @throws DiskLruCache.ClosedException
         * @throws DiskLruCache.FileNotExistException
         */
        void commit() throws IOException, DiskLruCache.EditorChangedException, DiskLruCache.ClosedException, DiskLruCache.FileNotExistException;

        /**
         * 写的过程中出现异常情况，中断写出
         */
        void abort();
    }
}