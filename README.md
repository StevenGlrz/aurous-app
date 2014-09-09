aurrous-app
===========

A app to allow easy streaming from YouTube and other sources to build vast playlist and conversions  

## What Is Aurous?

Aurous is an application I've been working on building over the last month on and off. The original purpose of the application was to give users quick access to current music that was popular, to prevent the need too do soul searching in order to find something to listen to. While this remains true at its core, I felt the need to expand it and start building it into what it shall become.
    
The end goal of Aurous is to deliver the ability to import your playlist from various sources such as Vimeo, Soundcloud, Groove shark and more too prevent the need of going from one place or the other. Listen to what you love in a centralized manner. 

## Beta 2.1.4

Change log here http://blog.poptart.me/post/96531815913/2-1-4-changelog

The actual decryption of video signatures is a completely scriptable sub system of the app, though it will be open source in latest releases; I decided to go ahead and make certain parts scriptable, Javascript is what is used, you can see the scripts in the /data/scripts/ folder.

```javascript
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
```

##Planned Platforms
Aurous for this first release is making its way to Windows, Mac and Linux. Once the desktop version has reached completion in terms of features/functions mobile versions will come officially to Android, IOS and Blackberry. 

##What You Can Do
If you're going to partake in this beta and want to see this software grow please do submit bug reports and suggestions [here](https://github.com/Codeusa/poptart-app/issues) to keep track of everything.

Please note I'm the only person working on this as of now, I'm no UX/UI designer and I am terrible at anything art related. So if you wish to contribute art or ui advice, feel free!


##Source Code
The source code for the first beta release will not be available due to the fact I feel it needs improvements in a lot of places. Truthfully I never intended to release this application to the public so I slacked a bit in code structure. Once it has been optimized (hopefully by the next revision) it should be up on this same hub.
 
##Contributing

Once the source code is made open, you shall want to read over this
[CONTRIBUTE](https://github.com/Codeusa/poptart-app/blob/master/CONTRIBUTE.md)

##Libs

Download all libs used in this project from here
http://aurous.me/downloads/lib.zip

##Requierments 
- Java 8
- An internet connection

##Downloads and Notes
Head over to [the official site](http://poptart.me) to get the official app.

Or follow [the official blog](http://blog.poptart.me) to see all the updates and whats to come. 
