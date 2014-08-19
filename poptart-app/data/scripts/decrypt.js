function findMatch(text, regexp) {
    var matches=text.match(regexp);
    return (matches)?matches[1]:null;
}

function isString(s) {
    return (typeof s==='string' || s instanceof String);
}

function isInteger(n) {
    return (typeof n==='number' && n%1==0);
}


function findSignatureCode() {

    	var decodeArray=[40, -1, 0, 40, -3, 0, 47,0], signatureLength=81;

        return decodeArray;
    
}


function decryptSignature(decodeArray, sig) {

    function swap(a,b){var c=a[0];a[0]=a[b%a.length];a[b]=c;return a};
    function decode(sig, arr) { // encoded decryption
        if (!(typeof sig==='string' || sig instanceof String)) return null;
        var sigA=sig.split('');
        for (var i=0;i<arr.length;i++) {
            var act=arr[i];
            if (!(typeof act==='number' && act%1==0)) return null; // not integer?
            sigA=(act>0)?swap(sigA, act):((act==0)?sigA.reverse():sigA.slice(-act));
        }
        var result=sigA.join('');
        return (result.length==81)?result:sig;
    }

    if (sig==null) return '';
    if (decodeArray) {
        var sig2=decode(sig, decodeArray);
        if (sig2 && sig2.length==81) return sig2;
    }
    return sig;
}


function getWorkingVideo(signature) {
    var testdecode = findSignatureCode();
    var testdecrypt = decryptSignature(testdecode, signature);
    return testdecrypt;
}