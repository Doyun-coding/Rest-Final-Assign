# 결산 과제
## Member (기본)


1. [o] 멤버를 생성하는 API
    1-1. [o] text/csv 지원
    1-2. [o] 중복 처리

2. [o] 멤버 조회
    2-1. [o] 단건 조회 API
    2-2. [o] 전체 조회 API
    2-3. [o] role은 소문자로 응답합니다 (enum을 이용)

3. [o] 멤버 단건을 조회하는 API
    3-1. [o] text/csv 지원
4. [o] 멤버 데이터는 Redis를 이용하여 저장합니다.
5. [ ] Password는 암호화 해서 저장합니다. 꼭!!! 저장할때부터!!
6. [o] Role은 ADMIN, MEMBER, GOOGLE 로 구분합니다 (조회시에는 소문자로 @JsonValue를 사용하지않고)

MEMBER
private String id;
private String name;
private String password;
private Integer age;
private Role role;

## 요청 공통 설정
1. [ ] 멤버 조회(GET)는 xml 형식으로 받을 수 있도록 합니다.
2. [o] HandlerMethodArgumentResolver를 이용해 Pageable에 기본값을 수정합니다.
    2-1. [o] 기본 페이징 size 5
    2-2. [o] MAX 페이징 10

## 인가
1. [ ] ADMIN만 접속가능한 페이지
2. [ ] MEMBER만 접속가능한 페이지
3. [ ] GOOGLE 만 접속가능한 페이지

각 페이지에 인가 TEST 케이스를 작성합니다 (200/403)

## 로그인
1. [ ] 사용자는 회원가입한 id,password로 로그인 가능합니다.
2. [ ] 레디스에 세션, 사용자 id 를 저장합니다.
 
    3-1. [ ] 레디스를 이용하여 등록합니다.
        3-1-1. [ ] 실패 카운트 데이터에 TTL을 넣어둡니다.
    3-2. [ ] 차단되었다고 메신저로 알림을 보냅니다.
4. [ ] 로그아웃 핸들러에서 쿠키 세션과, 레디스 세션을 제거합니다.

## OAUTH
1. [ ] 구글 로그인을 추가로 지원합니다.
2. [ ] 구글 로그인 성공시 기존 세션을 무효화합니다.
    2-1. [ ] 이미 아이디/패스워드로 로그인 사람은 로그인이 안되는 현상 제거.
3. [ ] 구글 로그인 한 사용자는 Role이 GOOGLE 로 지정합니다. 
4. [ ] 선택: 구글 로그인한 사용자도 서버 재시작시 로그인이 유지되도록 해주세요.(여러 방법을 고민해보세요)