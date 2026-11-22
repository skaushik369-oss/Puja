// Number of times each mantra is chanted. Change this one number to change it for all.
const DEFAULT_REPEAT_COUNT = 108;

const MANTRAS = [
 {
  "number": 1,
  "title": "Kamakhya Devi",
  "text": "Kleem Kleem Kamakhyayai Namah.",
  "speech": [
   "क्लीं क्लीं कामाख्यायै नमः"
  ],
  "note": null
 },
 {
  "number": 2,
  "title": "Maha Mrityunjay Jap",
  "text": "Om Tryambakam Yajamahe\nSugandhim Pushti vardhanam\nUrvarukamiva Bandhanaan\nMrityor mukshiya Maamritaat",
  "speech": [
   "ॐ त्र्यम्बकं यजामहे सुगन्धिं पुष्टिवर्धनम् । उर्वारुकमिव बन्धनान् मृत्योर्मुक्षीय मामृतात् ॥"
  ],
  "note": null
 },
 {
  "number": 3,
  "title": "Vishnu Sahasranama",
  "text": "Shrī Rāma Rāma Rāmeti Rame Rāme Manorame\nSahasranāma tat tulyam Rāma nāma varānane",
  "speech": [
   "श्री राम राम रामेति रमे रामे मनोरमे । सहस्रनाम तत्तुल्यं रामनाम वरानने ॥"
  ],
  "note": null
 },
 {
  "number": 4,
  "title": "Surya Beej Mantra",
  "text": "Om Hraam Hreem Hraum Sah Suryaya Namah",
  "speech": [
   "ॐ ह्रां ह्रीं ह्रौं सः सूर्याय नमः"
  ],
  "note": "Traditionally chanted × 21"
 },
 {
  "number": 5,
  "title": "Rahu Remedy",
  "text": "Om Raam Rahave Namah",
  "speech": [
   "ॐ रां राहवे नमः"
  ],
  "note": null
 },
 {
  "number": 6,
  "title": "Shani Beej Mantra",
  "text": "Om Praam Preem Praum Sah Shanaischaraya Namah",
  "speech": [
   "ॐ प्रां प्रीं प्रौं सः शनैश्चराय नमः"
  ],
  "note": null
 },
 {
  "number": 7,
  "title": "Hanuman Chalisa",
  "text": "|| Doha ||\n\nShri Guru Charan Saroj Raj, Nij Manu Mukuru Sudhari\nBaranau Raghubar Bimal Jasu, Jo Dayaku Phal Chari\nBuddhiheen Tanu Janike, Sumirau Pavan Kumar\nBal Buddhi Vidya Dehu Mohi, Harahu Kalesh Vikar\n\n|| Chaupai ||\n\nJai Hanuman Gyan Gun Sagar, Jai Kapis Tihu Lok Ujagar (1)\nRam Doot Atulit Bal Dhama, Anjani Putra Pavansut Nama (2)\nMahabir Bikram Bajrangi, Kumati Nivar Sumati Ke Sangi (3)\nKanchan Baran Biraj Subesa, Kanan Kundal Kunchit Kesa (4)\nHath Bajra Au Dhvaja Birajai, Kandhe Moonj Janeu Sajai (5)\nShankar Suvan Kesari Nandan, Tej Pratap Maha Jag Bandan (6)\nVidyavan Guni Ati Chatur, Ram Kaj Karibe Ko Aatur (7)\nPrabhu Charitra Sunibe Ko Rasiya, Ram Lakhan Sita Man Basiya (8)\nSukshma Roop Dhari Siyahi Dikhava, Bikat Roop Dhari Lank Jarava (9)\nBhim Roop Dhari Asur Sanhare, Ramchandra Ke Kaj Sanvare (10)\nLaye Sajivan Lakhan Jiyaye, Shri Raghubir Harashi Ur Laye (11)\nRaghupati Kinhi Bahut Badai, Tum Mam Priya Bharatahi Sam Bhai (12)\nSahas Badan Tumharo Jas Gavai, As Kahi Shripati Kanth Lagavai (13)\nSanakadik Brahmadi Munisa, Narad Sarad Sahit Ahisa (14)\nJam Kuber Digpal Jahan Te, Kabi Kobid Kahi Sake Kahan Te (15)\nTum Upkar Sugrivahi Kinha, Ram Milaye Raj Pad Dinha (16)\nTumharo Mantra Vibhishan Mana, Lankeshvar Bhaye Sab Jag Jana (17)\nJug Sahasra Jojan Par Bhanu, Leelyo Tahi Madhur Phal Janu (18)\nPrabhu Mudrika Meli Mukh Mahi, Jaladhi Langhi Gaye Acharaj Nahi (19)\nDurgam Kaj Jagat Ke Jete, Sugam Anugrah Tumhare Tete (20)\nRam Duare Tum Rakhvare, Hot Na Agya Binu Paisare (21)\nSab Sukh Lahai Tumhari Sarna, Tum Rakshak Kahu Ko Dar Na (22)\nAapan Tej Samharo Aapai, Teenon Lok Hank Te Kanpai (23)\nBhoot Pisach Nikat Nahi Aavai, Mahabir Jab Naam Sunavai (24)\nNasai Rog Harai Sab Peera, Japat Nirantar Hanumat Beera (25)\nSankat Te Hanuman Chhudavai, Man Kram Bachan Dhyan Jo Lavai (26)\nSab Par Ram Tapasvi Raja, Tin Ke Kaj Sakal Tum Saja (27)\nAur Manorath Jo Koi Lavai, Soi Amit Jivan Phal Pavai (28)\nCharon Jug Partap Tumhara, Hai Parsiddh Jagat Ujiyara (29)\nSadhu Sant Ke Tum Rakhvare, Asur Nikandan Ram Dulare (30)\nAshta Siddhi Nau Nidhi Ke Data, As Bar Deen Janaki Mata (31)\nRam Rasayan Tumhare Pasa, Sada Raho Raghupati Ke Dasa (32)\nTumhare Bhajan Ram Ko Pavai, Janam Janam Ke Dukh Bisravai (33)\nAnt Kaal Raghubar Pur Jai, Jahan Janma Hari Bhakt Kahai (34)\nAur Devta Chitt Na Dharai, Hanumat Sei Sarba Sukh Karai (35)\nSankat Katai Mitai Sab Peera, Jo Sumirai Hanumat Balbeera (36)\nJai Jai Jai Hanuman Gosain, Kripa Karahu Gurudev Ki Nain (37)\nJo Sat Bar Path Kar Koi, Chhutahi Bandi Maha Sukh Hoi (38)\nJo Yah Padhai Hanuman Chalisa, Hoy Siddhi Sakhi Gaurisa (39)\nTulsidas Sada Hari Chera, Keejai Nath Hriday Mah Dera (40)\n\n|| Doha ||\n\nPavan Tanay Sankat Haran, Mangal Murati Roop\nRam Lakhan Sita Sahit, Hriday Basahu Sur Bhoop",
  "speech": [
   "श्रीगुरु चरन सरोज रज, निज मनु मुकुरु सुधारि ।",
   "बरनउँ रघुबर बिमल जसु, जो दायकु फल चारि ॥",
   "बुद्धिहीन तनु जानिके, सुमिरौं पवन-कुमार ।",
   "बल बुधि बिद्या देहु मोहिं, हरहु कलेस बिकार ॥",
   "जय हनुमान ज्ञान गुन सागर । जय कपीस तिहुँ लोक उजागर ॥",
   "राम दूत अतुलित बल धामा । अंजनि-पुत्र पवनसुत नामा ॥",
   "महाबीर बिक्रम बजरंगी । कुमति निवार सुमति के संगी ॥",
   "कंचन बरन बिराज सुबेसा । कानन कुंडल कुंचित केसा ॥",
   "हाथ बज्र औ ध्वजा बिराजै । काँधे मूँज जनेऊ साजै ॥",
   "संकर सुवन केसरीनंदन । तेज प्रताप महा जग बंदन ॥",
   "बिद्यावान गुनी अति चातुर । राम काज करिबे को आतुर ॥",
   "प्रभु चरित्र सुनिबे को रसिया । राम लखन सीता मन बसिया ॥",
   "सूक्ष्म रूप धरि सियहिं दिखावा । बिकट रूप धरि लंक जरावा ॥",
   "भीम रूप धरि असुर सँहारे । रामचंद्र के काज सँवारे ॥",
   "लाय सजीवन लखन जियाये । श्रीरघुबीर हरषि उर लाये ॥",
   "रघुपति कीन्ही बहुत बड़ाई । तुम मम प्रिय भरतहि सम भाई ॥",
   "सहस बदन तुम्हरो जस गावैं । अस कहि श्रीपति कंठ लगावैं ॥",
   "सनकादिक ब्रह्मादि मुनीसा । नारद सारद सहित अहीसा ॥",
   "जम कुबेर दिगपाल जहाँ ते । कबि कोबिद कहि सके कहाँ ते ॥",
   "तुम उपकार सुग्रीवहिं कीन्हा । राम मिलाय राज पद दीन्हा ॥",
   "तुम्हरो मंत्र बिभीषन माना । लंकेस्वर भए सब जग जाना ॥",
   "जुग सहस्र जोजन पर भानू । लील्यो ताहि मधुर फल जानू ॥",
   "प्रभु मुद्रिका मेलि मुख माहीं । जलधि लाँघि गये अचरज नाहीं ॥",
   "दुर्गम काज जगत के जेते । सुगम अनुग्रह तुम्हरे तेते ॥",
   "राम दुआरे तुम रखवारे । होत न आज्ञा बिनु पैसारे ॥",
   "सब सुख लहै तुम्हारी सरना । तुम रच्छक काहू को डर ना ॥",
   "आपन तेज सम्हारो आपै । तीनों लोक हाँक तें काँपै ॥",
   "भूत पिसाच निकट नहिं आवै । महाबीर जब नाम सुनावै ॥",
   "नासै रोग हरै सब पीरा । जपत निरंतर हनुमत बीरा ॥",
   "संकट तें हनुमान छुड़ावै । मन क्रम बचन ध्यान जो लावै ॥",
   "सब पर राम तपस्वी राजा । तिन के काज सकल तुम साजा ॥",
   "और मनोरथ जो कोई लावै । सोइ अमित जीवन फल पावै ॥",
   "चारों जुग परताप तुम्हारा । है परसिद्ध जगत उजियारा ॥",
   "साधु संत के तुम रखवारे । असुर निकंदन राम दुलारे ॥",
   "अष्ट सिद्धि नौ निधि के दाता । अस बर दीन जानकी माता ॥",
   "राम रसायन तुम्हरे पासा । सदा रहो रघुपति के दासा ॥",
   "तुम्हरे भजन राम को पावै । जनम जनम के दुख बिसरावै ॥",
   "अंत काल रघुबर पुर जाई । जहाँ जन्म हरि-भक्त कहाई ॥",
   "और देवता चित्त न धरई । हनुमत सेइ सर्ब सुख करई ॥",
   "संकट कटै मिटै सब पीरा । जो सुमिरै हनुमत बलबीरा ॥",
   "जै जै जै हनुमान गोसाईं । कृपा करहु गुरुदेव की नाईं ॥",
   "जो सत बार पाठ कर कोई । छूटहि बंदि महा सुख होई ॥",
   "जो यह पढ़ै हनुमान चालीसा । होय सिद्धि साखी गौरीसा ॥",
   "तुलसीदास सदा हरि चेरा । कीजै नाथ हृदय महँ डेरा ॥",
   "पवन तनय संकट हरन, मंगल मूरति रूप ।",
   "राम लखन सीता सहित, हृदय बसहु सुर भूप ॥"
  ],
  "note": null
 }
];
