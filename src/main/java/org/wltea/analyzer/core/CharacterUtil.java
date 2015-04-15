/**
 * IK 中文分词  版本 5.0
 * IK Analyzer release 5.0
 * <p/>
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * 源代码由林良益(linliangyi2005@gmail.com)提供
 * 版权声明 2012，乌龙茶工作室
 * provided by Linliangyi and copyright 2012 by Oolong studio
 * <p/>
 * 字符集识别工具类
 */
package org.wltea.analyzer.core;

/**
 *
 * 字符集识别工具类
 */
class CharacterUtil {

    public static final int CHAR_USELESS = 0;

    public static final int CHAR_CUSTOM = 1;//自定义符号

    public static final int CHAR_ARABIC = 0X00000001;

    public static final int CHAR_ENGLISH = 0X00000002;

    public static final int CHAR_CHINESE = 0X00000004;

    public static final int CHAR_OTHER_CJK = 0X00000008;

    /**
     * 识别字符类型
     * @param input
     * @return int CharacterUtil定义的字符类型常量
     */
    static int identifyCharType(char input) {
        if (input >= '0' && input <= '9') {
            return CHAR_ARABIC;

        } else if ((input >= 'a' && input <= 'z')
                || (input >= 'A' && input <= 'Z')) {
            return CHAR_ENGLISH;

        } else {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(input);
            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
                //目前已知的中文字符UTF-8集合
                return CHAR_CHINESE;

            } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS //全角数字字符和日韩字符
                    //韩文字符集
                    || ub == Character.UnicodeBlock.HANGUL_SYLLABLES
                    || ub == Character.UnicodeBlock.HANGUL_JAMO
                    || ub == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
                    //日文字符集
                    || ub == Character.UnicodeBlock.HIRAGANA //平假名
                    || ub == Character.UnicodeBlock.KATAKANA //片假名
                    || ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS) {
                return CHAR_OTHER_CJK;
            }
        }
        //其他的不做处理的字符
        return CHAR_USELESS;
    }

    /**
     * 进行字符规格化（全角转半角，大写转小写处理）
     * @param input
     * @return char
     */
    static char regularize(char input) {
        if (input == 12288) {
            input = (char) 32;

        } else if (input > 65280 && input < 65375) {
            input = (char) (input - 65248);

        } else if (input >= 'A' && input <= 'Z') {
            input += 32;
        }

        return input;
    }

    /**
     * 自定义词典支持的符号,特殊符号字符集
     */
    public static boolean acceptChar(char input) {
        if (input == 0x0020)
            return false;
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(input);
        if (ub == Character.UnicodeBlock.BASIC_LATIN //0020-007F	基本拉丁文
                || ub == Character.UnicodeBlock.LATIN_1_SUPPLEMENT //拉丁文补充-1:¬°±等
                || (input >= 0x390 && input <= 0x3A9) //0390-03A9 大写希腊字母
                || (input >= 0x3B0 && input <= 0x3C9) //03B0-03C9 小写希腊字母
                || ub == Character.UnicodeBlock.ARROWS //2190-21FF 箭头符号
                || ub == Character.UnicodeBlock.MATHEMATICAL_OPERATORS //2200-22FF 数学运算符
                || ub == Character.UnicodeBlock.GEOMETRIC_SHAPES //25A0-25FF 几何图形
                ) {
            return true;
        }
        return false;
    }
}
