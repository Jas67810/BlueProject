/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unre.unreface.Base;

import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.unre.core.R;

/**
 * Created by cgspine on 2018/1/7.
 */

public abstract class BaseFragmentActivity extends QMUIFragmentActivity {
    private int mBaseState = BaseState.BASE_STATE_UNKNOW;
    public BaseFragmentActivity() {
        super();
    }
    @Override
    protected int getContextViewId() {
        return R.id.qmuidemo;
    }
    @Override
    public void onResume() {
        super.onResume();
        mBaseState = BaseState.BASE_STATE_ONRESUME;
    }
    @Override
    public void onPause() {
        super.onPause();
        mBaseState = BaseState.BASE_STATE_ONPAUSE;
    }
    @Override
    public void onStart() {
        super.onStart();
        mBaseState = BaseState.BASE_STATE_ONSTART;
    }
    @Override
    public void onStop() {
        super.onStop();
        mBaseState = BaseState.BASE_STATE_ONSTOP;
    }
    /////////////////////////////////////////////
    ////// API
    public int getBaseState(){
        return mBaseState;
    }
}
