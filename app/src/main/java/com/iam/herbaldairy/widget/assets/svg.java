package com.iam.herbaldairy.widget.assets;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.iam.herbaldairy.R;
import com.iam.herbaldairy.HerbalDairy;

public enum svg {
    add("iadd", R.raw.iadd),
    addred("iaddred", R.raw.iaddred),
    addwhite("iaddwhite", R.raw.iaddwhite),
    address("iaddress", R.raw.iaddress),
    addressgray("address.gray", R.raw.iaddressgray),
    addresswhite("iaddresswhite", R.raw.iaddresswhite),
    back("iback", R.raw.iback),
    back_white("ibackwhite", R.raw.ibackwhite),
    backgoundprofile("backgroundpattern", R.raw.backgroundpattern),
    baranina("ibaranina", R.raw.ibaranina),
    calendar("icalendar", R.raw.icalendar),
    calendarwhite("icalendarwhite", R.raw.icalendarwhite),
    cart("icart", R.raw.icart),
    cartred("icartred", R.raw.icartred),
    cartwhite("icartwhite", R.raw.icartwhite),
    contacts("icontacts", R.raw.icontacts),
    contactsgray("icontacts.gray", R.raw.icontactsgray),
    contactswhite("icontacts", R.raw.icontactswhite),
    delikatesyi("idelikatesyi", R.raw.idelikatesyi),
    delikatesyi0("idelikatesyi-0", R.raw.idelikatesyinull),
    dich("idich", R.raw.idich),
    down("idown", R.raw.idown),
    downred("idownred", R.raw.idownred),
    downwhite("idownwhite", R.raw.idownwhite),
    edit("iedit", R.raw.iedit),
    editwhite("ieditwhite", R.raw.ieditwhite),
    enter("ienter", R.raw.ienter),
    facebook("ifacebook", R.raw.ifacebook),
    facebookblue("ifacebookblue", R.raw.ifacebookblue),
    farmers("ifarmers", R.raw.ifarmers),
    farmerswhite("ifarmerswhite", R.raw.ifarmerswhite),
    forward("iforward", R.raw.iforward),
    forgot("iforgot", R.raw.iforgot),
    forward_white("iforwardwhite", R.raw.iforwardwhite),
    fruktyi("ifruktyi", R.raw.ifruktyi),
    help("ihelp", R.raw.ihelp),
    helpwhite("ihelpwhite", R.raw.ihelpwhite),
    git("igit", R.raw.igit),
    gitgray("igitgray", R.raw.igitgray),
    govyadina("igovyadina", R.raw.igovyadina),
    gplus("igplus", R.raw.igplus),
    gplusgray("igplusgray", R.raw.igplusgray),
    gribyi("igribyi", R.raw.igribyi),
    gus("igus", R.raw.igus),
    ikra("iikra", R.raw.iikra),
    indeyka("iindeyka", R.raw.iindeyka),
    isea("iicea", R.raw.iicea),
    kislomolochnyie_produktyi("ikislomolochnyie-produktyi", R.raw.ikislomolochnyie_produktyi),
    kopchenaya("ikopchenaya", R.raw.ikopchenaya),
    kozlyatina("ikozlyatina", R.raw.ikozlyatina),
    krolchatina("ikrolchatina", R.raw.ikrolchatina),
    kuritsa("ikuritsa", R.raw.ikuritsa),
    login("ilogin", R.raw.ilogin),
    logincircle("ilogincircle", R.raw.ilogincircle),
    logincirclewb("ilogincirclewb", R.raw.ilogincirclewb),
    loginwhite("iloginwhite", R.raw.iloginwhite),
    logobadge("ilogobadge", R.raw.ilogobadge),
    logobadgewhite("ilogobadgewhite", R.raw.ilogobadgewhite),
    logo("ilogo", R.raw.ilogo),
    logout("ilogout", R.raw.ilogout),
    logoutwhite("ilogoutwhte", R.raw.ilogoutwhite),
    love("ilove", R.raw.ilove),
    loveactive("iloveactive", R.raw.iloveactive),
    lovewhite("ilovewhite", R.raw.ilovewhite),
    makaronyi_i_lapsha("imakaronyi-i-lapsha", R.raw.imakaronyi_i_lapsha),
    message("imessage", R.raw.imessage),
    messagewhite("imessagewhite", R.raw.imessagewhite),
    minus("iminus", R.raw.iminus),
    moloko("imoloko", R.raw.imoloko),
    moreproduktyi("imoreproduktyi", R.raw.imoreproduktyi),
    next("inext", R.raw.inext),
    nextwhite("inextwhite", R.raw.inextwhite),
    news("inews", R.raw.inews),
    newswhite("inewswhite", R.raw.inewswhite),
    orehi_i_semechki("iorehi-i-semechki", R.raw.iorehi_i_semechki),
    order("iorder", R.raw.iorder),
    orderwhite("iorderwhite", R.raw.iorderwhite),
    ovoschi("iovoschi", R.raw.iovoschi),
    password("ipassword", R.raw.ipassword),
    passwordwhite("ipasswordwhite", R.raw.ipasswordwhite),
    payment("ipayment", R.raw.ipayment),
    paymentgray("ipayment.gray", R.raw.ipaymentgray),
    paymentwhite("ipaymentwhite", R.raw.ipaymentwhite),
    pencil("ipencil", R.raw.ipencil),
    pencilwhite("ipencilwhite", R.raw.ipencilwhite),
    perepelki("iperepelki", R.raw.iperepelki),
    plus("iplus", R.raw.iplus),
    plusred("iplusred", R.raw.iplusred),
    previous("iprev", R.raw.iprev),
    previouswhite("iprevwhite", R.raw.iprevwhite),
    prostokvasha("iprostokvasha", R.raw.iprostokvasha),
    productsprop("iproductsprop", R.raw.iproductsprop),
    products("iproducts", R.raw.iproducts),
    productswhite("iproductswhite", R.raw.iproductswhite),
    ruble("iruble", R.raw.iruble),
    rublel("irublel", R.raw.irublel),
    sas1("isas1", R.raw.isas1),
    sas2("isas2", R.raw.isas2),
    sas3("isas3", R.raw.isas3),
    sas4("isas4", R.raw.isas4),
    sas5("isas5", R.raw.isas5),
    search("isearch", R.raw.isearch),
    searchgray("isearchgray", R.raw.isearchgray),
    searchwhite("isearchwhite", R.raw.isearchwhite),
    select("iselect", R.raw.iselect),
    selectred("iselectred", R.raw.iselectred),
    settings("isettings", R.raw.isettings),
    settingswhite("isettingswhite", R.raw.isettingswhite),
    slivochnoe_maslo("islivochnoe-maslo", R.raw.islivochnoe_maslo),
    smetana("ismetana", R.raw.ismetana),
    solenaya("isolenaya", R.raw.isolenaya),
    solenya("isolenya", R.raw.isolenya),
    star("istar", R.raw.istarblack),
    star1("istar1", R.raw.istar1),
    star2("istar2", R.raw.istar2),
    star3("istar3", R.raw.istar3),
    star4("istar4", R.raw.istar4),
    star5("istar5", R.raw.istar5),
    starblack("istarblack", R.raw.istarblack),
    staryellow("istaryellow", R.raw.istaryellow),
    stargray("istargray", R.raw.istargray),
    straus("istraus", R.raw.istraus),
    suhofruktyi("isuhofruktyi", R.raw.isuhofruktyi),
    svejaya("isvejaya", R.raw.isvejaya),
    svejemorojenaya_ryiba("isvejemorojenaya-ryiba", R.raw.isvejemorojenaya_ryiba),
    svejemorojennyie_produktyi("isvejemorojennyie-produktyi", R.raw.isvejemorojennyie_produktyi),
    svinina("isvinina", R.raw.isvinina),
    syir("isyir", R.raw.isyir),
    time("itime", R.raw.itime),
    timegray("itimegray", R.raw.itimegray),
    toggle("itoggle", R.raw.itoggle),
    tsesarka("itsesarka", R.raw.itsesarka),
    tvorog("itvorog", R.raw.itvorog),
    up("iup", R.raw.iup),
    upwhite("iupwhite", R.raw.iupwhite),
    utka("iutka", R.raw.iutka),
    vk("ivk", R.raw.ivk),
    vkblue("ivkblue", R.raw.ivkblue),
    vyalenaya("ivyalenaya", R.raw.ivyalenaya),
    iwanteat("iwanteat", R.raw.iwanteat),
    x("ix", R.raw.ix),
    xwhite("ixwhite", R.raw.ixwhite),
    yagoda("iyagoda", R.raw.iyagoda),
    yogurt("iyogurt", R.raw.iyogurt),
    zagotovki_i_konservyi("izagotovki-i-konservyi", R.raw.izagotovki_i_konservyi),
    zelenwhite("izelenwhite", R.raw.izelenwhite),
    zelen("izelen", R.raw.izelen);

    svg(String name, int resourse) {
        this.name = name;
        this.resourse = resourse;
    }

    public Bitmap bitmap () {
        Bitmap bitmap;
        Drawable picture = drawable();

        if (drawable().getIntrinsicWidth() <= 0 || drawable().getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(picture.getIntrinsicWidth(), picture.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        picture.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        picture.draw(canvas);
        return bitmap;
    }

    public static svg svgByTag(String tag) {
        if (tag != null) {
            for (svg s : svg.values()) {
                if (s.name.equals(tag)) return s;
            }
        }
        return iwanteat;
    }

    public PictureDrawable drawable() {
        SVG svg = null;
        try {
            svg = SVG.getFromResource(HerbalDairy.context, resourse);
        } catch (SVGParseException e) {
            e.printStackTrace();
        }

        return new PictureDrawable(svg.renderToPicture());
    }

//    public BitmapDrawable bdTileYRepeat(int alpha, boolean repeat) {
//        RectF viewBox = svgSource.getDocumentViewBox();
//        float svgw = viewBox.width();
//        float svgh = viewBox.height();
//
//        int screenW = Decorator.getWidthBasedOnIPhone640(640);
//        float ratio = (float)screenW / svgw;
//
//        int pictureH = (int)(svgh * ratio);
//
//        PictureDrawable pictureDrawable = svgToDrawable();
//        Bitmap bitmap = Bitmap.createBitmap(screenW, pictureH, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        canvas.drawPicture(pictureDrawable.getPicture(), new RectF(0,0,screenW,pictureH));
//        BitmapDrawable bd = new BitmapDrawable(App.resources, bitmap);
//        bd.setAlpha(alpha);
//        if (repeat) bd.setTileModeY(Shader.TileMode.REPEAT);
//        return bd;
//    }

    String name;
    int resourse;
}
