CREATE TABLE dbcaccsnapshot (
	id integer NOT NULL,
	periodId integer NOT NULL,
	accId integer NOT NULL,
	amount decimal(20,6) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbccurravg (
	id integer NOT NULL,
	periodId integer,
	docId integer NOT NULL,
	docDate varchar(25),
	docType integer NOT NULL,
	docNo varchar(15) NOT NULL,
	rate decimal(20,4),
	locId integer NOT NULL,
	curId integer NOT NULL,
	accId integer NOT NULL,
	amountIn decimal(20,4) NOT NULL,
	amountOut decimal(20,4) NOT NULL,
	amountBalance decimal(20,4) NOT NULL,
	amountInBase decimal(20,4) NOT NULL,
	amountOutBase decimal(20,4) NOT NULL,
	amountBalanceBase decimal(20,4) NOT NULL,
	rateAvg decimal(20,4) NOT NULL,
	amountDiffBase decimal(20,4) NOT NULL,
	avgInOut integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbcstockavg (
	id integer NOT NULL,
	periodId integer NOT NULL,
	docId integer NOT NULL,
	transId integer NOT NULL,
	partnerId integer NOT NULL,
	locId integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	avgType integer NOT NULL,
	itemId integer NOT NULL,
	itemType integer NOT NULL,
	bundledId integer NOT NULL,
	qty decimal(20,6) NOT NULL,
	priceOrig decimal(20,6) NOT NULL,
	amountOrig decimal(20,6) NOT NULL,
	qtyIn decimal(20,6) NOT NULL,
	qtyOut decimal(20,6) NOT NULL,
	qtyBalance decimal(20,6) NOT NULL,
	amount decimal(20,6) NOT NULL,
	stockValue decimal(20,6) NOT NULL,
	priceAVG decimal(20,6) NOT NULL,
	profit decimal(20,6) NOT NULL,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbcstockfifo (
	id integer NOT NULL,
	fifoId integer NOT NULL,
	fifoType integer NOT NULL,
	periodId integer NOT NULL,
	transId integer,
	docId integer NOT NULL,
	docDate varchar(25),
	docType integer NOT NULL,
	docNo varchar(15) NOT NULL,
	refDocId integer NOT NULL,
	refDocType integer NOT NULL,
	refDocNo varchar(15) NOT NULL,
	partnerId integer,
	locId integer,
	itemId integer,
	itemType integer NOT NULL,
	bundledId integer NOT NULL,
	qtyIn decimal(20,4),
	qtyOut decimal(20,4),
	qtyOpen decimal(20,4),
	curId integer,
	rate decimal(20,6),
	priceOrig decimal(20,6),
	cogsOrig decimal(20,6),
	price decimal(20,6),
	cogsValue decimal(20,6),
	profit decimal(20,6),
	PRIMARY KEY (id)
);
	
CREATE TABLE dbcstockfifoin (
	id integer NOT NULL,
	periodId integer NOT NULL,
	transId integer NOT NULL,
	docId integer NOT NULL,
	docDate varchar(25),
	docType integer NOT NULL,
	docNo varchar(15),
	partnerId integer NOT NULL,
	locId integer NOT NULL,
	itemId integer NOT NULL,
	itemType integer NOT NULL,
	bundledId integer NOT NULL,
	qtyIn decimal(20,4) NOT NULL,
	qtyOut decimal(20,4) NOT NULL,
	qtyOpen decimal(20,4) NOT NULL,
	curId integer NOT NULL,
	rate decimal(20,6) NOT NULL,
	priceOrig decimal(20,6) NOT NULL,
	price decimal(20,6) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbcstockfifoout (
	id integer NOT NULL,
	periodId integer NOT NULL,
	transId integer NOT NULL,
	docId integer NOT NULL,
	docDate varchar(25),
	docType integer NOT NULL,
	docNo varchar(15),
	partnerId integer NOT NULL,
	locId integer NOT NULL,
	itemId integer NOT NULL,
	itemType integer NOT NULL,
	bundledId integer NOT NULL,
	qtyIn decimal(20,4) NOT NULL,
	qtyOut decimal(20,4) NOT NULL,
	qtyOpen decimal(20,4) NOT NULL,
	curId integer NOT NULL,
	rate decimal(20,6) NOT NULL,
	priceOrig decimal(20,6) NOT NULL,
	price decimal(20,6) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbcstocksnapshot (
	id integer NOT NULL,
	periodId integer NOT NULL,
	fifoId integer NOT NULL,
	docId integer NOT NULL,
	transId integer NOT NULL,
	partnerId integer NOT NULL,
	locId integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	itemId integer NOT NULL,
	itemType integer NOT NULL,
	qtyIn decimal(20,6) NOT NULL,
	qty decimal(20,6) NOT NULL,
	priceOrig decimal(20,6) NOT NULL,
	price decimal(20,6) NOT NULL,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dblcheque (
	id integer NOT NULL,
	locId integer NOT NULL,
	docId integer NOT NULL,
	docType integer NOT NULL,
	accId integer NOT NULL,
	curId integer NOT NULL,
	parId integer NOT NULL,
	chequeNo varchar(15) NOT NULL,
	chequeInDate varchar(50) NOT NULL,
	chequeDate varchar(50) NOT NULL,
	clearingDate varchar(50),
	chequeType integer NOT NULL,
	rate decimal(10) NOT NULL,
	amount decimal(10) NOT NULL,
	settled integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmaccount (
	id integer NOT NULL,
	code varchar(30) NOT NULL,
	name varchar(50),
	isBank integer NOT NULL,
	isCredit integer NOT NULL,
	isDebit integer NOT NULL,
	machine varchar(10),
	notes text,
	status integer NOT NULL,
	userCreateId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmaccountpos (
	id integer NOT NULL,
	accId integer NOT NULL,
	warId integer NOT NULL,
	machine varchar(10) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmauth (
	id integer NOT NULL,
	code varchar(50),
	name varchar(100),
	status integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmcoa (
	id integer NOT NULL,
	parentId integer NOT NULL,
	code varchar(30) NOT NULL,
	name varchar(50) NOT NULL,
	level integer,
	isLeaf integer NOT NULL,
	groupId integer NOT NULL,
	notes text,
	status integer NOT NULL,
	userAccessId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmcoatemplate (
	id integer NOT NULL,
	parentId integer,
	code varchar(30) NOT NULL,
	name varchar(50),
	level integer NOT NULL,
	isLeaf integer NOT NULL,
	groupId integer NOT NULL,
	notes text,
	status integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmcurrency (
	id integer NOT NULL,
	code varchar(3) NOT NULL,
	name varchar(50),
	rate decimal(20,6) NOT NULL,
	spread decimal(20,6) NOT NULL,
	opkurs integer NOT NULL,
	isBase integer NOT NULL,
	notes text,
	status integer NOT NULL,
	userAccessId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmcusttype (
	id integer NOT NULL,
	code varchar(30) NOT NULL,
	notes text,
	status integer NOT NULL,
	userCreateId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmdepartment (
	id integer NOT NULL,
	code varchar(30) NOT NULL,
	name varchar(50),
	notes text,
	status integer NOT NULL,
	userCreateId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmemployee (
	id integer NOT NULL,
	code varchar(30) NOT NULL,
	name varchar(50),
	job integer NOT NULL,
	commision decimal(20,6) NOT NULL,
	notes text,
	status integer NOT NULL,
	userAccessId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmglmapping (
	id integer NOT NULL,
	code varchar(2),
	title varchar(50),
	coaId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmgroupcoa (
	id integer NOT NULL,
	code varchar(10),
	name varchar(50),
	status integer,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmitem (
	id integer NOT NULL,
	depId integer NOT NULL,
	curId integer NOT NULL,
	code varchar(30) NOT NULL,
	name varchar(50) NOT NULL,
	vendorCode varchar(30) NOT NULL,
	type integer NOT NULL,
	level integer NOT NULL,
	minStock integer NOT NULL,
	snIn integer NOT NULL,
	snOut integer NOT NULL,
	snCorrelation integer NOT NULL,
	isSell integer NOT NULL,
	isBuy integer NOT NULL,
	isMenu integer NOT NULL,
	isTaxed integer NOT NULL,
	isService integer NOT NULL,
	notes text,
	status integer NOT NULL,
	userCreateId integer NOT NULL,
	image varchar(255),
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmitemcategory (
	id integer NOT NULL,
	parentId integer NOT NULL,
	code varchar(30) NOT NULL,
	name varchar(50) NOT NULL,
	level integer NOT NULL,
	userAccessId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmitemcategorydetail (
	id integer NOT NULL,
	categoryId integer NOT NULL,
	itemId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmitemmaterial (
	id integer NOT NULL,
	itemId integer NOT NULL,
	compId integer NOT NULL,
	compUnitId integer NOT NULL,
	qty float(12) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmitemunit (
	id integer NOT NULL,
	itemid integer NOT NULL,
	code varchar(30) NOT NULL,
	isBase integer NOT NULL,
	conversion float(12) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmjournalmapping (
	id integer NOT NULL,
	docType integer,
	coaId integer NOT NULL,
	type integer NOT NULL,
	isEdit integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmjournalperiod (
	id integer NOT NULL,
	year integer NOT NULL,
	month integer NOT NULL,
	strMonth varchar(10) NOT NULL,
	startDate varchar(50) NOT NULL,
	endDate varchar(50) NOT NULL,
	isPosted integer NOT NULL,
	isClosed integer NOT NULL,
	isLocked integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmlocacc (
	id integer NOT NULL,
	locId integer NOT NULL,
	accId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmlocation (
	id integer NOT NULL,
	code varchar(30) NOT NULL,
	name varchar(50),
	batchNo varchar(14),
	lastClosingPeriod varchar(50),
	notes text,
	status integer NOT NULL,
	userAccessId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmlocpar (
	id integer NOT NULL,
	locId integer NOT NULL,
	parId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmmenu (
	id integer NOT NULL,
	code varchar(50),
	name varchar(100),
	notes text,
	tab varchar(30),
	status integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmpartner (
	id integer NOT NULL,
	ctpId integer NOT NULL,
	empId integer NOT NULL,
	code varchar(30) NOT NULL,
	name varchar(50) NOT NULL,
	type integer NOT NULL,
	arLimit decimal(20,6) NOT NULL,
	arTerm integer NOT NULL,
	apLimit decimal(20,6) NOT NULL,
	apTerm integer NOT NULL,
	npwp varchar(20),
	address text,
	addressShip text,
	city varchar(50),
	cityShip varchar(50),
	province varchar(50),
	provinceShip varchar(50),
	country varchar(50),
	countryShip varchar(50),
	postalCode varchar(5),
	postalCodeShip varchar(5),
	phone1 varchar(20),
	phone1Ship varchar(20),
	phone2 varchar(20),
	phone2Ship varchar(20),
	fax varchar(20),
	faxShip varchar(20),
	email varchar(100),
	emailShip varchar(100),
	nameCP varchar(50),
	phone1CP varchar(20),
	phone2CP varchar(20),
	email1CP varchar(100),
	email2CP varchar(100),
	notes text,
	status integer NOT NULL,
	userCreateId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmprice (
	id integer NOT NULL,
	itemId integer NOT NULL,
	ctpId integer NOT NULL,
	price decimal(20,6) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmsession (
	id integer NOT NULL,
	locId integer NOT NULL,
	code varchar(30) NOT NULL,
	openTime varchar NOT NULL,
	closeTime varchar NOT NULL,
	notes text,
	status integer NOT NULL,
	userAccessId integer,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmuser (
	id integer NOT NULL,
	code varchar(30) NOT NULL,
	name varchar(50),
	password varchar(32) NOT NULL,
	email varchar(100),
	notes text,
	status integer NOT NULL,
	userAccessId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbmwarehouse (
	id integer NOT NULL,
	locId integer NOT NULL,
	code varchar(30) NOT NULL,
	name varchar(50),
	notes text,
	status integer NOT NULL,
	userAccessId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbsaccuse (
	id integer NOT NULL,
	accId integer NOT NULL,
	userId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbscount (
	id integer NOT NULL,
	// locId integer NOT NULL,
	prefix varchar(3) NOT NULL,
	doctype integer NOT NULL,
	counter integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbslocuse (
	id integer NOT NULL,
	locId integer NOT NULL,
	userId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbslog (
	id integer NOT NULL,
	userId integer NOT NULL,
	menuId integer NOT NULL,
	dataId integer NOT NULL,
	date varchar(50) NOT NULL,
	action integer NOT NULL,
	notes text NOT NULL,
	notes1 text NOT NULL,
	notes2 text NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbslogdtl (
	id integer NOT NULL,
	userId integer NOT NULL,
	menuId integer NOT NULL,
	dataId integer NOT NULL,
	machineId varchar(100) NOT NULL,
	date varchar(50) NOT NULL,
	formName varchar(50) NOT NULL,
	action text NOT NULL,
	notes text NOT NULL,
	notes1 text NOT NULL,
	notes2 text NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbsoptions (
	userId integer NOT NULL,
	posParId integer NOT NULL,
	posEmpId integer NOT NULL,
	posCtpId integer NOT NULL,
	posParName varchar(50),
	posTerm integer NOT NULL,
	posParNotes text,
	posViewCode integer NOT NULL,
	posWarId integer NOT NULL
);
	
CREATE TABLE dbssystem (
	version integer NOT NULL,
	locId integer NOT NULL,
	company varchar(50) NOT NULL,
	address text NOT NULL,
	city varchar(30) NOT NULL,
	phone varchar(20) NOT NULL,
	npwp varchar(30) NOT NULL,
	firstInstall integer NOT NULL,
	priceMethod integer NOT NULL,
	hppIncludeDisc integer NOT NULL,
	taxed decimal(10,2) NOT NULL,
	minStockOption integer,
	moneyOption integer,
	stockOption integer,
	taxCode varchar(30) NOT NULL,
	postDate varchar(50) NOT NULL
);
	
CREATE TABLE dbstaxcount (
	id integer NOT NULL,
	docType integer NOT NULL,
	docId integer NOT NULL,
	fpNo varchar(20),
	fpType varchar(3),
	fpLoc varchar(3),
	PRIMARY KEY (id)
);
	
CREATE TABLE dbsuserauth (
	id integer NOT NULL,
	userId integer NOT NULL,
	authId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbsusermenu (
	id integer NOT NULL,
	userId integer NOT NULL,
	menuId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtaccinoutdoc (
	id integer NOT NULL,
	locId integer NOT NULL,
	curId integer NOT NULL,
	accId integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	rate decimal(20,6) NOT NULL,
	paymentType integer NOT NULL,
	paymentRef varchar(20),
	chequeDueDate varchar(25),
	grandTotal decimal(20,6) NOT NULL,
	docNotes text,
	void integer NOT NULL,
	printTime integer NOT NULL,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtaccinouttrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	coaId integer NOT NULL,
	line integer NOT NULL,
	description varchar(50),
	amount decimal(20,6) NOT NULL,
	Notes text,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtacctransferdoc (
	id integer NOT NULL,
	accId integer NOT NULL,
	accDestId integer NOT NULL,
	docId integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	rate decimal(20,6) NOT NULL,
	rateDest decimal(20,6) NOT NULL,
	amount decimal(20,6) NOT NULL,
	amountDest decimal(20,6) NOT NULL,
	grandTotal decimal(20,6) NOT NULL,
	settled integer NOT NULL,
	isCreateIn integer NOT NULL,
	docNotes text,
	void integer NOT NULL,
	printTime integer NOT NULL,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtamtmemodoc (
	id integer NOT NULL,
	locId integer NOT NULL,
	partnerId integer NOT NULL,
	curId integer NOT NULL,
	accId integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	term integer NOT NULL,
	rate decimal(20,6) NOT NULL,
	name varchar(50),
	address text,
	paymentType integer NOT NULL,
	paymentRef varchar(20),
	chequeDueDate varchar(25),
	grandTotal decimal(20,6) NOT NULL,
	paidTotal decimal(20,6) NOT NULL,
	settled integer NOT NULL,
	docNotes text,
	void integer NOT NULL,
	printTime integer NOT NULL,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtamtmemotrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	line integer NOT NULL,
	amount decimal(20,6) NOT NULL,
	notes varchar(225),
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtamtnotedoc (
	id integer NOT NULL,
	locId integer NOT NULL,
	partnerId integer NOT NULL,
	curId integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	term integer NOT NULL,
	rate decimal(20,6) NOT NULL,
	name varchar(50),
	address text,
	grandTotal decimal(20,6) NOT NULL,
	paidTotal decimal(20,6) NOT NULL,
	settled integer NOT NULL,
	docNotes text,
	void integer NOT NULL,
	printTime integer NOT NULL,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtamtnotetrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	coaId integer NOT NULL,
	line integer NOT NULL,
	description varchar(50),
	amount decimal(20,6) NOT NULL,
	notes varchar(225),
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtamtpaymentdoc (
	id integer NOT NULL,
	partnerId integer NOT NULL,
	accId integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	term integer NOT NULL,
	paymentType integer NOT NULL,
	paymentRef varchar(20),
	grandTotal decimal(20,6) NOT NULL,
	paidTotal decimal(20,6) NOT NULL,
	overpaid decimal(20,6) NOT NULL,
	isPos integer NOT NULL,
	sessionId integer NOT NULL,
	docNotes text,
	void integer NOT NULL,
	userCreateId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtamtpaymenttrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	paidDocId integer NOT NULL,
	paidDocType integer NOT NULL,
	line integer NOT NULL,
	discountStr varchar(50),
	discountAmount decimal(20,6) NOT NULL,
	amount decimal(20,6) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtchequedoc (
	id integer NOT NULL,
	chequeId integer NOT NULL,
	locId integer NOT NULL,
	accId integer NOT NULL,
	curId integer NOT NULL,
	partnerId integer NOT NULL,
	docType integer NOT NULL,
	docNo varchar(15) NOT NULL,
	docDate varchar(50) NOT NULL,
	rate decimal(10) NOT NULL,
	amount decimal(10) NOT NULL,
	docNotes text,
	void integer NOT NULL,
	post integer NOT NULL,
	printTime integer NOT NULL,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtdiscdoc (
	id integer NOT NULL,
	code varchar(30) NOT NULL,
	name varchar(50),
	priority integer NOT NULL,
	status integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtdiscglobal (
	id integer NOT NULL,
	docId integer NOT NULL,
	type integer NOT NULL,
	itemId integer NOT NULL,
	depId integer NOT NULL,
	categoryId integer NOT NULL,
	unitId integer NOT NULL,
	line integer NOT NULL,
	qty decimal(20,6) NOT NULL,
	qtyDef decimal(20,6) NOT NULL,
	qtyM decimal(20,6) NOT NULL,
	discountStr varchar(50) NOT NULL,
	discountAmount decimal(20,6) NOT NULL,
	bundledId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtdiscitem (
	id integer NOT NULL,
	docId integer NOT NULL,
	itemId integer NOT NULL,
	depId integer NOT NULL,
	categoryId integer NOT NULL,
	unitId integer NOT NULL,
	line integer NOT NULL,
	qty decimal(20,6) NOT NULL,
	qtyDef decimal(20,6) NOT NULL,
	qtyM decimal(20,6) NOT NULL,
	typeFree integer NOT NULL,
	itemIdFree integer NOT NULL,
	unitIdFree integer NOT NULL,
	qtyFree decimal(20,6) NOT NULL,
	qtyDefFree decimal(20,6) NOT NULL,
	qtyMFree decimal(20,6) NOT NULL,
	discountStr varchar(50) NOT NULL,
	discountAmount decimal(20,6) NOT NULL,
	price decimal(20,6) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtdiscreq (
	id integer NOT NULL,
	docId integer NOT NULL,
	isAllDate integer NOT NULL,
	startDate varchar(25),
	endDate varchar(25),
	isAllTime integer NOT NULL,
	startTime varchar,
	endTime varchar,
	isAllDay integer NOT NULL,
	dayReq varchar(30) NOT NULL,
	isAllPayment integer NOT NULL,
	accid integer NOT NULL,
	isMinPurchase integer NOT NULL,
	minPurchase decimal(20,6) NOT NULL,
	custReq integer NOT NULL,
	isAllCust integer NOT NULL,
	custId integer NOT NULL,
	isAllCustType integer NOT NULL,
	custTypeId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtgeneraljournaldoc (
	id integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	docNotes text,
	void integer NOT NULL,
	printTime integer NOT NULL,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtgeneraljournaltrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	coaId integer NOT NULL,
	line integer NOT NULL,
	coaName varchar(50) NOT NULL,
	debit decimal(20,6) NOT NULL,
CRedit decimal(20,6) NOT NULL,
	Notes text,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtitemadjdoc (
	id integer NOT NULL,
	locId integer NOT NULL,
	warId integer NOT NULL,
	coaIdAdjusment integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	isSaldoAwal integer NOT NULL,
	docNotes text,
	void integer NOT NULL,
	printTime integer NOT NULL,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtitemadjtrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	itemId integer NOT NULL,
	unitId integer NOT NULL,
	line integer NOT NULL,
	itemName varchar(50) NOT NULL,
	qty decimal(20,6) NOT NULL,
	qtyDef decimal(20,6) NOT NULL,
	qtyM decimal(20,6) NOT NULL,
	price decimal(20,6) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtitemproductiondoc (
	id integer NOT NULL,
	locId integer NOT NULL,
	warId integer NOT NULL,
	itemId integer NOT NULL,
	unitId integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	qty integer NOT NULL,
	docNotes text,
	void integer NOT NULL,
	printTime integer NOT NULL,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtitemproductiontrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	itemId integer NOT NULL,
	unitId integer NOT NULL,
	line integer NOT NULL,
	itemName varchar(50),
	qty decimal(20,6) NOT NULL,
	qtyDef decimal(20,6) NOT NULL,
	qtyM decimal(20,6) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtitemsndoc (
	id integer NOT NULL,
	itemId integer NOT NULL,
	SN varchar(50) NOT NULL,
	warId integer NOT NULL,
	availability integer NOT NULL,
	isTransfer integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtitemsntrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	docIdIn integer NOT NULL,
	docDateIn varchar(50),
	docTypeIn integer NOT NULL,
	transIdIn integer NOT NULL,
	warIdIn integer NOT NULL,
	docIdOut integer NOT NULL,
	docDateOut varchar(50),
	docTypeOut integer NOT NULL,
	transIdOut integer NOT NULL,
	warIdOut integer NOT NULL,
	transIdSettledIn integer NOT NULL,
	docTypeSettledIn integer NOT NULL,
	transIdSettledOut integer NOT NULL,
	docTypeSettledOut integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtitemsntransfertrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	docIdOut integer NOT NULL,
	docDateOut varchar(50),
	docTypeOut integer NOT NULL,
	transIdOut integer NOT NULL,
	warIdOut integer NOT NULL,
	docIdIn integer NOT NULL,
	docDateIn varchar(50),
	docTypeIn integer NOT NULL,
	transIdIn integer NOT NULL,
	warIdIn integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtitemtransferdoc (
	id integer NOT NULL,
	LocId integer NOT NULL,
	warId integer NOT NULL,
	locDestId integer NOT NULL,
	warDestId integer NOT NULL,
	docId integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	eta integer NOT NULL,
	settled integer NOT NULL,
	docNotes text,
	void integer NOT NULL,
	printTime integer NOT NULL,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtitemtransfertrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	itemId integer NOT NULL,
	unitId integer NOT NULL,
	line integer NOT NULL,
	itemName varchar(50) NOT NULL,
	qty decimal(20,6) NOT NULL,
	qtyDef decimal(20,6) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtpriceupdatedoc (
	id integer NOT NULL,
	locId integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	docNotes text,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtpriceupdatetrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	curId integer NOT NULL,
	itemId integer NOT NULL,
	unitId integer NOT NULL,
	ctpId integer NOT NULL,
	oldPrice decimal(20,6) NOT NULL,
	price decimal(20,6) NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtpurchasedoc (
	id integer NOT NULL,
	warId integer NOT NULL,
	partnerId integer NOT NULL,
	curId integer NOT NULL,
	docId integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	docVendorNo varchar(30) NOT NULL,
	term integer NOT NULL,
	rate decimal(20,6) NOT NULL,
	name varchar(50) NOT NULL,
	address text,
	addressShipment text,
	city varchar(30) NOT NULL,
	cityShipment varchar(30) NOT NULL,
	postalCode varchar(10) NOT NULL,
	postalCodeShipment varchar(10) NOT NULL,
	phone1 varchar(20) NOT NULL,
	phone1Shipment varchar(20) NOT NULL,
	phone2 varchar(20) NOT NULL,
	phone2Shipment varchar(20) NOT NULL,
	subTotal decimal(20,6) NOT NULL,
	discountPercent decimal(20,6) NOT NULL,
	discountAmount decimal(20,6) NOT NULL,
	taxDPP decimal(20,6) NOT NULL,
	taxPercent decimal(20,6) NOT NULL,
	taxAmount decimal(20,6) NOT NULL,
	grandTotal decimal(20,6) NOT NULL,
	paidTotal decimal(20,6) NOT NULL,
	isCash integer NOT NULL,
	paidCash decimal(20,6) NOT NULL,
	settled integer NOT NULL,
	docNotes text,
	void integer NOT NULL,
	printTime integer NOT NULL,
	userAccessId integer NOT NULL,
CReateUserId integer NOT NULL,
	locId integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtpurchasetrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	transid integer NOT NULL,
	itemId integer NOT NULL,
	unitId integer NOT NULL,
	line integer NOT NULL,
	itemName varchar(50) NOT NULL,
	qty decimal(20,6) NOT NULL,
	qtyDef decimal(20,6) NOT NULL,
	qtySettled decimal(20,6) NOT NULL,
	price decimal(20,6) NOT NULL,
	discountStr varchar(50) NOT NULL,
	discountAmount decimal(20,6) NOT NULL,
	isTaxed integer NOT NULL,
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtsalesdoc (
	id integer NOT NULL,
	// warId integer NOT NULL,
	partnerId integer NOT NULL,
	// locId integer NOT NULL,
	// curId integer NOT NULL,
	// empId integer NOT NULL,
	promoId integer NOT NULL,
	// docId integer NOT NULL,
	docDate varchar(50) NOT NULL,
	docNo varchar(15) NOT NULL,
	docType integer NOT NULL,
	// term integer NOT NULL,
	// rate decimal(20,6) NOT NULL,
	// name varchar(50),
	// address text,
	// addressShipment text,
	// city varchar(30),
	// cityShipment varchar(30),
	// postalCode varchar(10),
	// postalCodeShipment varchar(10),
	// phone1 varchar(20),
	// phone1Shipment varchar(20),
	// phone2 varchar(20),
	// phone2Shipment varchar(20),
	subTotal decimal(20,6) NOT NULL,
	discountPercent decimal(20,6) NOT NULL,
	discountAmount decimal(20,6) NOT NULL,
	taxDPP decimal(20,6) NOT NULL,
	taxPercent decimal(20,6) NOT NULL,
	taxAmount decimal(20,6) NOT NULL,
	grandTotal decimal(20,6) NOT NULL,
	paidTotal decimal(20,6) NOT NULL,
	// commision float(12) NOT NULL,
	// isPos integer NOT NULL,
	sessionId integer NOT NULL,
	isCash integer NOT NULL,
	paidCash decimal(20,6) NOT NULL,
	// settled integer NOT NULL,
	docNotes text,
	void integer NOT NULL,
	// printTax integer NOT NULL,
	printTime integer NOT NULL,
	// userAccessId integer NOT NULL,
	userCreateId integer NOT NULL,
	// machine varchar(50),
	PRIMARY KEY (id)
);
	
CREATE TABLE dbtsalestrans (
	id integer NOT NULL,
	docId integer NOT NULL,
	// transid integer NOT NULL,
	itemId integer NOT NULL,
	// unitId integer NOT NULL,
	line integer NOT NULL,
	itemName varchar(50),
	qty decimal(20,6) NOT NULL,
	// qtyDef decimal(20,6) NOT NULL,
	// qtyM decimal(20,6) NOT NULL,
	// qtySettled decimal(20,6) NOT NULL,
	price decimal(20,6) NOT NULL,
	// discountStr varchar(50),
	discountAmount decimal(20,6) NOT NULL,
	// isTaxed integer NOT NULL,
	// bundledId integer NOT NULL,
	// refDocNo varchar(50),
	// refDocId integer,
	PRIMARY KEY (id)
);;