/*
 * Copyright (C) 2017 Peng fei Pan <sky@xiaopan.me>
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

package me.xiaopan.sketch.uri;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;

import me.xiaopan.sketch.datasource.DataSource;
import me.xiaopan.sketch.datasource.FileDataSource;
import me.xiaopan.sketch.request.DownloadResult;

public class FileUriModel extends UriModel {

    public static final String SCHEME = "/";

    @Override
    protected boolean match(@NonNull String uri) {
        return !TextUtils.isEmpty(uri) && uri.startsWith(SCHEME);
    }

    /**
     * 获取 uri 所真正包含的内容部分，但对于 "/sdcard/test.png" 格式的 uri 来说就是返回它自己
     *
     * @param uri 图片 uri
     * @return uri 所真正包含的内容部分，但对于 "/sdcard/test.png" 格式的 uri 来说就是返回它自己
     */
    @Override
    public String getUriContent(@NonNull String uri) {
        return uri;
    }

    @NonNull
    @Override
    public DataSource getDataSource(@NonNull Context context, @NonNull String uri, DownloadResult downloadResult) {
        return new FileDataSource(new File(uri));
    }
}
