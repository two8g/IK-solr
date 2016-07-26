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
 */
package org.wltea.analyzer.core;

/**
 * IK词元对象
 */
public class Lexeme implements Comparable<Lexeme> {
    //lexemeType常量
    //未知
    public static final int TYPE_UNKNOWN = -1;
    //英文数字混合
    public static final int TYPE_LETTER = 1;
    //数字
    public static final int TYPE_ARABIC = 2;
    //中文词元
    public static final int TYPE_CNWORD = 3;
    //日韩文字
    public static final int TYPE_OTHER_CJK = 4;
    //中文单字
    public static final int TYPE_CNCHAR = 5;
    //中文数词
    public static final int TYPE_CNUM = 6;
    //中文量词
    public static final int TYPE_COUNT = 7;
    //中文数量词
    public static final int TYPE_CQUAN = 8;
    //英文
    public static final int TYPE_ENGLISH = 9;
    //英文2词组
    public static final int TYPE_ENGLISH_2 = 10;
    //英文3词组
    public static final int TYPE_ENGLISH_3 = 11;
    //纯数字字母和上下标
    public static final int TYPE_LATEX_ONLYNLPB = 0;
    //LATEX 公式
    public static final int TYPE_LATEX = 100;
    //分式\frac{}{}
    public static final int TYPE_LATEX_FRAC = 101;
    //分子frac{}
    public static final int TYPE_LATEX_FRAC_1 = 102;
    //根式\sqrt[]{}
    public static final int TYPE_LATEX_SQRT = 103;
    //次根式\sqrt[]
    public static final int TYPE_LATEX_SQRT_1 = 103;
    //(...)
    public static final int TYPE_LATEX_BRACKET = 104;
    //(...)中的...
    public static final int TYPE_LATEX_BRACKET_1 = 105;
    //{...}
    public static final int TYPE_LATEX_BRACE = 106;
    //{...}中的...
    public static final int TYPE_LATEX_BRACE_1 = 107;
    //幂次
    public static final int TYPE_LATEX_POWER = 109;


    //词元的起始位移
    protected int offset;
    //词元的相对起始位置
    protected int begin;
    //词元的长度
    protected int length;
    //词元文本
    protected String lexemeText;
    //词元类型
    protected int lexemeType;

    public Lexeme() {
        super();
    }

    public Lexeme(int offset, int begin, int length, int lexemeType) {
        this.offset = offset;
        this.begin = begin;
        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        this.length = length;
        this.lexemeType = lexemeType;
    }

    public Lexeme(int offset, int begin, int length, int lexemeType, String lexemeText) {
        this.offset = offset;
        this.begin = begin;
        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        this.length = length;
        this.lexemeType = lexemeType;
        this.setLexemeText(lexemeText);
    }

    /*
     * 判断词元相等算法
     * 起始位置偏移、起始位置、终止位置相同
     * @see java.lang.Object#equals(Object o)
     */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (o instanceof Lexeme) {
            Lexeme other = (Lexeme) o;
            if (this.offset == other.getOffset()
                    && this.begin == other.getBegin()
                    && this.length == other.getLength()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /*
     * 词元哈希编码算法
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int absBegin = getBeginPosition();
        int absEnd = getEndPosition();
        return (absBegin * 37) + (absEnd * 31) + ((absBegin * absEnd) % getLength()) * 11;
    }

    /*
     * 词元在排序集合中的比较算法
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Lexeme other) {
        //起始位置优先
        if (this.begin < other.getBegin()) {
            return -1;
        } else if (this.begin == other.getBegin()) {
            //词元长度优先
            if (this.length > other.getLength()) {
                return -1;
            } else if (this.length == other.getLength()) {
                return 0;
            } else {//this.length < other.getLength()
                return 1;
            }

        } else {//this.begin > other.getBegin()
            return 1;
        }
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getBegin() {
        return begin;
    }

    /**
     * 获取词元在文本中的起始位置
     *
     * @return int
     */
    public int getBeginPosition() {
        return offset + begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    /**
     * 获取词元在文本中的结束位置
     *
     * @return int
     */
    public int getEndPosition() {
        return offset + begin + length;
    }

    /**
     * 获取词元的字符长度
     *
     * @return int
     */
    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        if (this.length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        this.length = length;
    }

    /**
     * 获取词元的文本内容
     *
     * @return String
     */
    public String getLexemeText() {
        if (lexemeText == null) {
            return "";
        }
        return lexemeText;
    }

    public void setLexemeText(String lexemeText) {
        if (lexemeText == null) {
            this.lexemeText = "";
            this.length = 0;
        } else {
            this.lexemeText = lexemeText;
            this.length = lexemeText.length();
        }
    }

    /**
     * 获取词元类型
     *
     * @return int
     */
    public int getLexemeType() {
        return lexemeType;
    }

    /**
     * 获取词元类型标示字符串
     *
     * @return String
     */
    public String getLexemeTypeString() {
        switch (lexemeType) {

            case TYPE_ENGLISH:
                return "ENGLISH";

            case TYPE_ENGLISH_2:
                return "ENGLISH_2";

            case TYPE_ENGLISH_3:
                return "ENGLISH_3";

            case TYPE_ARABIC:
                return "ARABIC";

            case TYPE_LETTER:
                return "LETTER";

            case TYPE_CNWORD:
                return "CN_WORD";

            case TYPE_CNCHAR:
                return "CN_CHAR";

            case TYPE_OTHER_CJK:
                return "OTHER_CJK";

            case TYPE_COUNT:
                return "COUNT";

            case TYPE_CNUM:
                return "CNUM";

            case TYPE_CQUAN:
                return "CQUAN";

            case TYPE_LATEX:
                return "LATEX";

            case TYPE_LATEX_FRAC:
                return "LATEX_FRAC";

            case TYPE_LATEX_FRAC_1:
                return "LATEX_FRAC_1";

            case TYPE_LATEX_BRACKET:
                return "LATEX_BRACE";

            case TYPE_LATEX_BRACKET_1:
                return "LATEX_BRACE_1";

            case TYPE_LATEX_SQRT:
                return "LATEX_SQRT";

            case TYPE_LATEX_BRACE:
                return "LATEX_BIGBRACE";

            case TYPE_LATEX_BRACE_1:
                return "LATEX_BIGBRACE_1";

            case TYPE_LATEX_ONLYNLPB:
                return "LATEX_ONLYNLPB";

            case TYPE_LATEX_POWER:
                return "LATEX_POWER";

            default:
                return "UNKNOWN";
        }
    }


    public void setLexemeType(int lexemeType) {
        this.lexemeType = lexemeType;
    }

    /**
     * 合并两个相邻的词元
     *
     * @param l
     * @param lexemeType
     * @return boolean 词元是否成功合并
     */
    public boolean append(Lexeme l, int lexemeType) {
        if (l != null && this.getEndPosition() == l.getBeginPosition()) {
            this.length += l.getLength();
            this.lexemeType = lexemeType;
            return true;
        } else {
            return false;
        }
    }


    /**
     *
     */
    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.getBeginPosition()).append("-").append(this.getEndPosition());
        strbuf.append(" : ").append(this.lexemeText).append(" : \t");
        strbuf.append(this.getLexemeTypeString());
        return strbuf.toString();
    }


}
