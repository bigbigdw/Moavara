package com.example.moavara.DataBase

//테이블이라고 생각하고, 테이블에 들어갈 속성값을 넣기
//파이어베이스는 RDBMS와 다르기 때문에 테이블이라는 개념이 없음. 원래는 키값이라고 부름
class animal {
    var name //동물 이름
            : String? = null
    var kind //동물 종류
            : String? = null

    constructor() {} // 생성자 메서드

    //getter, setter 설정
    fun getname(): String? {
        return name
    }

    fun setname(name: String?) {
        this.name = name
    }

    fun getkind(): String? {
        return kind
    }

    fun setkind(kind: String?) {
        this.kind = kind
    }

    //값을 추가할때 쓰는 함수, MainActivity에서 addanimal함수에서 사용할 것임.
    constructor(name: String?, kind: String?) {
        this.name = name
        this.kind = kind
    }
}