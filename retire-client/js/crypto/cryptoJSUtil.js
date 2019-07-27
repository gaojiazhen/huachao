var KEY = "abcdefgabcdefg12";

/**
 * Aes加密
 * @param {Object} word
 */
function encrypt(word) {
	var key = CryptoJS.enc.Utf8.parse(KEY);
	var srcs = CryptoJS.enc.Utf8.parse(word);
	var encrypted = CryptoJS.AES.encrypt(srcs, key, {
		mode: CryptoJS.mode.ECB,
		padding: CryptoJS.pad.Pkcs7
	});
	return encrypted.toString();
}

/**
 * md5加密
 * @param {Object} word
 */
function md5encode(word) {
	return CryptoJS.MD5(word).toString();
}

/**
 * md5 + AES 双重加密
 * @param {Object} word
 */
function doubleEncrypt(word) {
	//console.log("进入加密时:"+word);
	var rr = word.toString();
	var temp = this.md5encode(rr);
	//console.log("md5加密后:"+temp);
	temp = this.encrypt(temp, null);
	//console.log("aes加密后:"+temp);
	return temp;
}