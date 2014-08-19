/**
 * Created by Andrew on 8/18/14.
 */
function isInteger(n) {
    return typeof n == "number" && isFinite(n) && n % 1 === 0;
}



function findSignatureCode(sourceCode) {
    function findMatch(text, regexp) {
        var matches = text.match(regexp);
        return (matches) ? matches[1] : null;
    }
    var signatureFunctionName = findMatch(sourceCode,
        /\.signature\s*=\s*([a-zA-Z_$][\w$]*)\([a-zA-Z_$][\w$]*\)/);
    if (signatureFunctionName == null) return "nosigfun";
    signatureFunctionName = signatureFunctionName.replace('$', '\\$');
    var regCode = new RegExp('function \\s*' + signatureFunctionName +
        '\\s*\\([\\w$]*\\)\\s*{[\\w$]*=[\\w$]*\\.split\\(""\\);(.+);return [\\w$]*\\.join');
    var functionCode = findMatch(sourceCode, regCode);

    if (functionCode == null) return "nofunction";

    var reverseFunctionName = findMatch(sourceCode,
        /([\w$]*)\s*:\s*function\s*\(\s*[\w$]*\s*\)\s*{\s*(?:return\s*)?[\w$]*\.reverse\s*\(\s*\)\s*}/);

    if (reverseFunctionName) reverseFunctionName = reverseFunctionName.replace('$', '\\$');
    var sliceFunctionName = findMatch(sourceCode,
        /([\w$]*)\s*:\s*function\s*\(\s*[\w$]*\s*,\s*[\w$]*\s*\)\s*{\s*(?:return\s*)?[\w$]*\.(?:slice|splice)\(.+\)\s*}/);
    if (sliceFunctionName) sliceFunctionName = sliceFunctionName.replace('$', '\\$');

    var regSlice = new RegExp('\\.(?:' + 'slice' + (sliceFunctionName ? '|' + sliceFunctionName : '') +
        ')\\s*\\(\\s*(?:[a-zA-Z_$][\\w$]*\\s*,)?\\s*([0-9]+)\\s*\\)'); // .slice(5) sau .Hf(a,5)
    var regReverse = new RegExp('\\.(?:' + 'reverse' + (reverseFunctionName ? '|' + reverseFunctionName : '') +
        ')\\s*\\([^\\)]*\\)'); // .reverse() sau .Gf(a,45)
    var regSwap = new RegExp('[\\w$]+\\s*\\(\\s*[\\w$]+\\s*,\\s*([0-9]+)\\s*\\)');
    var regInline = new RegExp('[\\w$]+\\[0\\]\\s*=\\s*[\\w$]+\\[([0-9]+)\\s*%\\s*[\\w$]+\\.length\\]');
    var functionCodePieces = functionCode.split(';');
    var decodeArray = [];
    var signatureLength = 81;
    for (var i = 0; i < functionCodePieces.length; i++) {
        functionCodePieces[i] = functionCodePieces[i].trim();
        var codeLine = functionCodePieces[i];
        if (codeLine.length > 0) {
            var arrSlice = codeLine.match(regSlice);

            var arrReverse = codeLine.match(regReverse);
            if (arrSlice && arrSlice.length >= 2) { // slice
                var slice = parseInt(arrSlice[1], 10);
                if (isInteger(slice)) {
                    decodeArray.push(-slice);
                    signatureLength += slice;
                } else break;
            } else if (arrReverse && arrReverse.length >= 1) { // reverse
                decodeArray.push(0);
            } else if (codeLine.indexOf('[0]') >= 0) { // inline swap
                if (i + 2 < functionCodePieces.length &&
                    functionCodePieces[i + 1].indexOf('.length') >= 0 &&
                    functionCodePieces[i + 1].indexOf('[0]') >= 0) {
                    var inline = findMatch(functionCodePieces[i + 1], regInline);
                    inline = parseInt(inline, 10);
                    decodeArray.push(inline);
                    i += 2;
                } else break;
            } else if (codeLine.indexOf(',') >= 0) { // swap
                var swap = findMatch(codeLine, regSwap);
                swap = parseInt(swap, 10);
                if (isInteger(swap) && swap > 0) {
                    decodeArray.push(swap);
                } else break;
            } else break;
        }
    }

    if (decodeArray) {

        return decodeArray
    }
}

function decryptSignature(decodeArray, sig) {

    function swap(a, b) {
        var c = a[0];
        a[0] = a[b % a.length];
        a[b] = c;
        return a
    };

    function decode(sig, arr) { // encoded decryption
        if (!(typeof sig === 'string' || sig instanceof String)) return null;
        var sigA = sig.split('');
        for (var i = 0; i < arr.length; i++) {
            var act = arr[i];
            if (!(typeof act === 'number' && act % 1 == 0)) return null; // not integer?
            sigA = (act > 0) ? swap(sigA, act) : ((act == 0) ? sigA.reverse() : sigA.slice(-act));
        }
        var result = sigA.join('');
        return (result.length == 81) ? result : sig;
    }

    if (sig == null) return '';
    if (decodeArray) {
        var sig2 = decode(sig, decodeArray);
        if (sig2 && sig2.length == 81) return sig2;
    }
    return sig;
}



function getWorkingVideo(signature, videoSource) {
    var testdecode = findSignatureCode(videoSource);
    var testdecrypt = decryptSignature(testdecode, signature);
    return testdecrypt;
}
