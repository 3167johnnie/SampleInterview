You can fix the alignment issue by making only small modifications in your existing CSS instead of rewriting everything.

Replace ONLY these sections in your current CSS.

---

## 1. FIX `.prd-tab ul li`

Current:

```css id="v6h6m3"
.prd-tab ul li{ width:25%;font-family: 'Roboto-Regular'; float:left; background:url(../images/prd-divider.jpg) repeat-y left top;position:relative; }
```

Replace with:

```css id="2e6z6u"
.prd-tab ul li{
    width:25%;
    font-family:'Roboto-Regular';
    float:left;
    background:url(../images/prd-divider.jpg) repeat-y left top;
    position:relative;
    min-height:230px;
    box-sizing:border-box;
}
```

---

## 2. FIX `.prd-tab ul li a`

Current:

```css id="6r84fp"
.prd-tab ul li a{ color:#fff;float:left;padding:8px 20px; transition:0.9s linear; }
```

Replace with:

```css id="93ec0w"
.prd-tab ul li a{
    color:#fff;
    float:left;
    padding:20px;
    transition:0.3s linear;
    width:100%;
    height:230px;
}
```

---

## 3. FIX `h2 ALIGNMENT`

Current:

```css id="7wfxms"
.prd-tab h2{font-size:24px;font-family: 'Roboto-Regular';}
```

Replace with:

```css id="cmyb6d"
.prd-tab h2{
    font-size:24px;
    font-family:'Roboto-Regular';
    min-height:65px;
    line-height:30px;
    margin-top:10px;
    margin-bottom:15px;
}
```

---

## 4. FIX PARAGRAPH ALIGNMENT

Add BELOW `.prd-tab h2`

```css id="oc7v7t"
.prd-tab ul li p{
    line-height:20px;
    min-height:80px;
}
```

---

## 5. FIX INSURANCE IMAGE SPACING

Add:

```css id="6r9rbx"
.prd-tab ul li img{
    margin-bottom:10px;
    max-width:55px;
    height:auto;
}
```

---

## 6. FIX TYPO

Current:

```css id="6h7d67"
.menu-li{poition:relative; transition:0.9s linear;}
```

Replace with:

```css id="1xq3qm"
.menu-li{
    position:relative;
    transition:0.3s linear;
}
```

---

## 7. OPTIONAL IMPORTANT FIX (BEST)

Your text lengths are different.

Add this:

```css id="z8cg4m"
.prd-tab ul li a:hover{
    text-decoration:none;
}
```

Because currently:

```css id="5glqhz"
.prd-tab ul li a:hover,.prd-tab ul li.active
```

is applying background inconsistently.

---

This will fix:

* unequal heights
* broken alignment
* text spacing mismatch
* insurance image push-down
* hover inconsistency

WITHOUT changing your old float-based architecture.
