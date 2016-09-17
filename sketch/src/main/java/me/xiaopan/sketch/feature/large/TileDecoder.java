/*
 * Copyright (C) 2016 Peng fei Pan <sky@xiaopan.me>
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

package me.xiaopan.sketch.feature.large;

import android.text.TextUtils;
import android.util.Log;

import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.util.KeyCounter;

/**
 * 碎片解码器
 */
class TileDecoder {

    private static final String NAME = "TileDecoder";

    private KeyCounter initKeyCounter;
    private ImageRegionDecoder decoder;
    private LargeImageViewer largeImageViewer;
    private boolean running;
    private boolean initializing;

    public TileDecoder(LargeImageViewer largeImageViewer) {
        this.largeImageViewer = largeImageViewer;
        this.initKeyCounter = new KeyCounter();
    }

    /**
     * 设置新的图片
     */
    void setImage(String imageUri) {
        clean("setImage");

        if (decoder != null) {
            decoder.recycle();
            decoder = null;
        }

        if (!TextUtils.isEmpty(imageUri)) {
            running = initializing = true;
            largeImageViewer.getTileExecutor().submitInit(imageUri, initKeyCounter);
        } else {
            running = initializing = false;
        }
    }

    /**
     * 解码
     */
    void decodeTile(Tile tile) {
        if (!isReady()) {
            if (Sketch.isDebugMode()) {
                Log.w(Sketch.TAG, NAME + ". not ready. decodeTile. " + tile.getInfo());
            }
            return;
        }

        tile.decoder = decoder;
        largeImageViewer.getTileExecutor().submitDecodeTile(tile.getKey(), tile);
    }

    void clean(String why) {
        if (Sketch.isDebugMode()) {
            Log.w(Sketch.TAG, NAME + ". clean. " + why);
        }

        initKeyCounter.refresh();
    }

    void recycle(String why) {
        if (Sketch.isDebugMode()) {
            Log.w(Sketch.TAG, NAME + ". recycle. " + why);
        }

        if (decoder != null) {
            decoder.recycle();
        }
    }

    void initCompleted(String imageUri, ImageRegionDecoder decoder) {
        if (Sketch.isDebugMode()) {
            Log.d(Sketch.TAG, NAME + ". init completed. " + imageUri);
        }

        initializing = false;
        this.decoder = decoder;
    }

    void initFailed(String imageUri, Exception e) {
        if (Sketch.isDebugMode()) {
            Log.d(Sketch.TAG, NAME + ". init failed. " + e.getMessage() + ". " + imageUri);
        }

        initializing = false;
    }

    boolean isReady() {
        return running && decoder != null && decoder.isReady();
    }

    boolean isInitializing() {
        return running && initializing;
    }

    public ImageRegionDecoder getDecoder() {
        return decoder;
    }
}