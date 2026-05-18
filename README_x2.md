You can fix the alignment issue by making only small modifications in your existing CSS instead of rewriting everything.

Replace ONLY these sections in your current CSS.

⸻

1. FIX .prd-tab ul li

Current:

.prd-tab ul li{ width:25%;font-family: 'Roboto-Regular'; float:left; background:url(../images/prd-divider.jpg) repeat-y left top;position:relative; }

Replace with:

.prd-tab ul li{
    width:25%;
    font-family:'Roboto-Regular';
    float:left;
    background:url(../images/prd-divider.jpg) repeat-y left top;
    position:relative;
    min-height:230px;
    box-sizing:border-box;
}

⸻

2. FIX .prd-tab ul li a

Current:

.prd-tab ul li a{ color:#fff;float:left;padding:8px 20px; transition:0.9s linear; }

Replace with:

.prd-tab ul li a{
    color:#fff;
    float:left;
    padding:20px;
    transition:0.3s linear;
    width:100%;
    height:230px;
}

⸻

3. FIX h2 ALIGNMENT

Current:

.prd-tab h2{font-size:24px;font-family: 'Roboto-Regular';}

Replace with:

.prd-tab h2{
    font-size:24px;
    font-family:'Roboto-Regular';
    min-height:65px;
    line-height:30px;
    margin-top:10px;
    margin-bottom:15px;
}

⸻

4. FIX PARAGRAPH ALIGNMENT

Add BELOW .prd-tab h2

.prd-tab ul li p{
    line-height:20px;
    min-height:80px;
}

⸻

5. FIX INSURANCE IMAGE SPACING

Add:

.prd-tab ul li img{
    margin-bottom:10px;
    max-width:55px;
    height:auto;
}

⸻

6. FIX TYPO

Current:

.menu-li{poition:relative; transition:0.9s linear;}

Replace with:

.menu-li{
    position:relative;
    transition:0.3s linear;
}

⸻

7. OPTIONAL IMPORTANT FIX (BEST)

Your text lengths are different.

Add this:

.prd-tab ul li a:hover{
    text-decoration:none;
}

Because currently:

.prd-tab ul li a:hover,.prd-tab ul li.active

is applying background inconsistently.

⸻

This will fix:

* unequal heights
* broken alignment
* text spacing mismatch
* insurance image push-down
* hover inconsistency

WITHOUT changing your old float-based architecture.
