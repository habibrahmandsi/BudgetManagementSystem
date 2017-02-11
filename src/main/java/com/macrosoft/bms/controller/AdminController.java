package com.macrosoft.bms.controller;

import com.macrosoft.bms.data.PaymentMethodType;
import com.macrosoft.bms.data.dao.Impl.AdminDAOImpl;
import com.macrosoft.bms.data.dao.Impl.UserDaoImpl;
import com.macrosoft.bms.data.model.*;
import com.macrosoft.bms.util.Constants;
import com.macrosoft.bms.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

@Controller
public class AdminController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminDAOImpl adminDaoImpl;
    @Autowired
    private UserDaoImpl userDaoImpl;


    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }


    @RequestMapping(value = "/*/getImage.do", method = RequestMethod.GET)
    @ResponseBody
    public byte[] getImage(HttpServletRequest request) throws IOException {
//        logger.debug("***************  Image Controller ***************");
        String imgId = request.getParameter("imgId") != null ? request.getParameter("imgId") : "";
        String gender = request.getParameter("sex") != null ? request.getParameter("sex") : "common";
        int imageSize = request.getParameter("imgSize") != null ? Integer.parseInt(request.getParameter("imgSize")) : 1;

//        logger.debug("imgId:" + imgId + " gender:" + gender);
        String uploadPath = Constants.UPLOAD_BASE_DIR;
        String uploadFolderForStudents = Constants.UPLOAD_EMP_PHOTO_DIR;
        String serverHomeFolder = Utils.getApplicationPropertyValue("image.upload.dir");//System.getProperty("user.home");
        File file = null;
        ClassLoader classLoader = getClass().getClassLoader();
        if (imgId != null && !StringUtils.isEmpty(imgId) && !"null".equals(imgId)) {
            String dirForStudentsPhoto = serverHomeFolder + File.separator + uploadPath + File.separator + uploadFolderForStudents;
//            logger.debug("dirForStudentsPhoto:" + dirForStudentsPhoto);
            dirForStudentsPhoto += File.separator + imgId;
            file = new File(dirForStudentsPhoto);
        } else {
            //Get file from resources folder
            /*if ("male".equals(gender.toLowerCase())) {
                file = new File(classLoader.getResource("/images/employee-icon-4.png").getFile());
            } else if ("female".equals(gender.toLowerCase())) {
                file = new File(classLoader.getResource("/images/emp-5.png").getFile());
            } else if ("common".equals(gender.toLowerCase())) {
                file = new File(classLoader.getResource("/images/employeeIcon.png").getFile());
            }*/
        }
//        return Files.readAllBytes(file.toPath());
        return createThumbNailImage(file, imageSize);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/landing.do")
    public String landingPage(HttpServletRequest request, @RequestParam(value = "action", required = false) String action, Model model) {
        System.out.println("----------------- tar/landing ---------------");

        try {
            Double totalExpenseAmount = adminDaoImpl.getTotalExpenseAmount(null, null);
            Double allDeposit = adminDaoImpl.getTotalDepositAmount(0);
            List<ShareHolder> shareHolderList = adminDaoImpl.getAllShareHolderList();
            model.addAttribute("shareHolderList",shareHolderList);
            model.addAttribute("allDeposit",Utils.getTwoDigitAfterDecimal(allDeposit));
            model.addAttribute("totalExpenseAmount",Utils.getTwoDigitAfterDecimal(totalExpenseAmount));
            model.addAttribute("totalAvailableAmount",Utils.getTwoDigitAfterDecimal(allDeposit - totalExpenseAmount));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "tar/landing";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/upsertDeposit.do")
    public String upsertDeposit(HttpServletRequest request, Model model) {
        logger.info("********* Deposit create Controller ************");
        Integer depositId = request.getParameter("depositId") != null ? Integer.parseInt(request.getParameter("depositId")) : 0;
        Deposit deposit = new Deposit();
        List<Map> installmentList = new ArrayList<>();
        try {
            installmentList = adminDaoImpl.getInstallment();
            if (depositId > 0) {
                deposit = adminDaoImpl.getDepositById(depositId);
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial("<b>"+Utils.getLoggedInUsername()+"</b> "+ Utils.getMessageBundlePropertyValue("audit.update.deposit.message"), request);

            } else {
                logger.debug("for Save :" + depositId);
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial("<b>"+Utils.getLoggedInUsername()+"</b> "+Utils.getMessageBundlePropertyValue("audit.create.deposit.message"), request);
            }
        } catch (Exception e) {
            logger.error("ERROR:" + e);
        }
        model.addAttribute("installmentList", installmentList);
        model.addAttribute("paymentMethodList", Arrays.asList(PaymentMethodType.values()));
        model.addAttribute("deposit", deposit);
        return "admin/deposit";
    }

    /*
* Method for viewing landing Page
* @param HttpServletRequest request, Model model
* @return type String( or any .jsp File)
*
*/
    @RequestMapping(value = "/admin/upsertDeposit.do", method = RequestMethod.POST)
    public String upsertDepositPost(HttpServletRequest request, @ModelAttribute("deposit") Deposit deposit, Model model) {

        logger.debug("*************** Upsert Deposit POST Controller ***************");
//        logger.debug("::shareHolder:" + shareHolder);
        Integer depositId = deposit.getId();
        try {
            User user = userDaoImpl.getUserByUserName(Utils.getLoggedInUsername());
            deposit.setCreatedBy(user);
            deposit.setDate(new Date());
            adminDaoImpl.saveOrUpdate(deposit);

            if (depositId != null){
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("deposit.update.success.msg"));
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial(Utils.getLoggedInUsername()+" have successfully updated "+" the data of reference No:'"+deposit.getReferenceNo()+"'.", request);
            }
            else{

                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("deposit.save.success.msg"));
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial(Utils.getLoggedInUsername()+" have successfully saved "+" the data of reference No:'"+deposit.getReferenceNo()+"'.", request);
            }
        } catch (Exception ex) {
            logger.error("CERROR:: Failed:" + ex);
            if (depositId != null && depositId > 0) {
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("deposit.update.failed.msg")+" "+ex.getMessage());
            } else {
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("deposit.save.failed.msg")+" "+ex.getMessage());
            }
        }

        return "redirect:/admin/depositList.do";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/depositList.do")
    public String depositListView(Model model) {
        logger.info("********* Deposit List Controller ************");
        try {
        Double allDeposit = adminDaoImpl.getTotalDepositAmount(0);
        model.addAttribute("allDeposit",allDeposit == null? 0: Utils.getTwoDigitAfterDecimal(allDeposit));
        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load all deposit summation data:: " + ex);
        }
        return "admin/depositList";
    }

    @RequestMapping(value = "/admin/getDeposits.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getDeposits(HttpServletRequest request) throws Exception {
        logger.info(":: Get Deposits List Ajax Controller ::");
        DataModelBean dataModelBean = new DataModelBean();
         /* this params is for dataTables */
        String[] tableColumns = ",name,fathersName,mothersName,mobile,installmentName,amount,method,referenceNo,createdBy".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String sortColName = "";

        sortColName = tableColumns[iSortColIndex];
        Map<String, Object> userDataMap;

        try {

            int totalRecords = Math.toIntExact(adminDaoImpl.getDepositDataSize());
            logger.info("totalRecords:" + totalRecords + " length:" + length);
            if (length < 0) {
                userDataMap = adminDaoImpl.getDeposits(start, totalRecords + 1, sortColName, sortType, searchKey);
            } else {
                userDataMap = adminDaoImpl.getDeposits(start, length, sortColName, sortType, searchKey);
            }

            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!StringUtils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load relativity instance details data:: " + ex);
        }

        return dataModelBean;
    }

    /*
* Method for viewing shareHolder create/update Page
* @param HttpServletRequest request, Model model
* @return type String( or any .jsp File)
*
*/
    @RequestMapping(value = "/admin/upsertShareHolder.do", method = RequestMethod.GET)
    public String upsertEmployee(HttpServletRequest request, Model model) {
        logger.debug("*************** Share Holder get Controller ***************");
        Integer shareHolderId = request.getParameter("shareHolderId") != null ? Integer.parseInt(request.getParameter("shareHolderId")) : 0;
        ShareHolder shareHolder = new ShareHolder();
        try {
            if (shareHolderId > 0) {
                shareHolder = adminDaoImpl.getShareHolderById(shareHolderId);
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial("<b>"+Utils.getLoggedInUsername()+"</b> "+ Utils.getMessageBundlePropertyValue("audit.update.sh.view.message")
                        +" '"+shareHolder.getName()+"'", request);

            } else {
                logger.debug("for Save :" + shareHolderId);
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial("<b>"+Utils.getLoggedInUsername()+"</b> "+Utils.getMessageBundlePropertyValue("audit.new.sh.view.message"), request);
            }
        } catch (Exception e) {
            logger.error("ERROR:" + e);
        }
        model.addAttribute("shareHolder", shareHolder);
        return "admin/shareHolder";
    }

    /*
   * Method for viewing landing Page
   * @param HttpServletRequest request, Model model
   * @return type String( or any .jsp File)
   *
   */
    @RequestMapping(value = "/admin/upsertShareHolder.do", method = RequestMethod.POST)
    public String upsertShareHolderPost(HttpServletRequest request, @ModelAttribute("shareHolder") ShareHolder shareHolder, Model model) {

        logger.debug("*************** Upsert ShareHolder POST Controller ***************");
//        logger.debug("::shareHolder:" + shareHolder);
        String uploadPath = Constants.UPLOAD_BASE_DIR;
        String uploadFolderForStudents = Constants.UPLOAD_EMP_PHOTO_DIR;
        String serverHomeFolder = Utils.getApplicationPropertyValue("image.upload.dir");//System.getProperty("user.home");
        String photoId = Utils.getTodaysDate().getTime() + ".png";
        String uploadPathForStudents = (serverHomeFolder + File.separator + uploadPath + File.separator + uploadFolderForStudents + File.separator + "" + photoId);
        File dirForStudentsPhoto = new File(serverHomeFolder + File.separator + uploadPath + File.separator + uploadFolderForStudents);
        Integer employeeId = shareHolder.getId();
        MultipartFile multipartFile = shareHolder.getFileData();
        try {
            if (!dirForStudentsPhoto.exists()) {  // make directory
                dirForStudentsPhoto.mkdirs();
                logger.debug("SMNLOG:Directory created....");
            }
            if (multipartFile != null && !StringUtils.isEmpty(multipartFile.getOriginalFilename())){
                multipartFile.transferTo(new File(uploadPathForStudents));  // upload file to specific directory
                shareHolder.setPhotoPath(uploadPathForStudents);
                shareHolder.setPhotoName(photoId);
                logger.debug("SMNLOG:File uploaded....");
            }

            adminDaoImpl.saveOrUpdate(shareHolder);

            if (employeeId != null){

                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("shareHolder.update.success.msg"));

                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial(Utils.getLoggedInUsername()+" have successfully updated "+" the data of '"+shareHolder.getName()+"'.", request);
            }
            else{

                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("shareHolder.save.success.msg"));
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial(Utils.getLoggedInUsername()+" have successfully saved "+" the data of '"+shareHolder.getName()+"'.", request);
            }
        } catch (Exception ex) {
            logger.error("CERROR:: Failed:" + ex);
            if (shareHolder.getId() != null && shareHolder.getId() > 0) {
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("shareHolder.update.failed.msg")+" "+ex.getMessage());
            } else {
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("shareHolder.save.failed.msg")+" "+ex.getMessage());
            }
        }

        return "redirect:./shareHolderList.do";
    }

    /*
* Method for viewing shareHolder create/update Page
* @param HttpServletRequest request, Model model
* @return type String( or any .jsp File)
*
*/
    @RequestMapping(value = "/admin/shareHolderList.do", method = RequestMethod.GET)
    public String shareHolderListView(HttpServletRequest request, Model model) {
        logger.debug("*************** Share Holder get Controller ***************");
        return "admin/shareHolderList";
    }

    @RequestMapping(value = "/admin/getShareHolders.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean shareHolderList(HttpServletRequest request) throws Exception {
        logger.info(":: Get share Holder List List Ajax Controller ::");
        DataModelBean dataModelBean = new DataModelBean();
         /* this params is for dataTables */
        String[] tableColumns = ",name,fathersName,mothersName,sex,religion,nationalId,spouseName,email,mobile".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 1;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String sortColName = "";
        System.out.println("--------------->"+tableColumns[1]);
        sortColName = tableColumns[iSortColIndex];
        Map<String, Object> userDataMap;

        try {
            int totalRecords = Math.toIntExact(adminDaoImpl.getShareHolderDataSize());
            logger.info("totalRecords:" + totalRecords + " length:" + length+" sortColName:"+sortColName);
            if (length < 0) {
                userDataMap = adminDaoImpl.getShareHolders(start, totalRecords + 1, sortColName, sortType, searchKey);
            } else {
                userDataMap = adminDaoImpl.getShareHolders(start, length, sortColName, sortType, searchKey);
            }

            logger.debug("dataModelBean::" + dataModelBean);
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!StringUtils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load relativity instance details data:: " + ex);
        }

        return dataModelBean;
    }

    private byte[] createThumbNailImage(File inputFile, int imageSize) {
        logger.debug("**************** createThumbNailImage ***********************");
        int thumbnailWidth = Constants.THUMBNAIL_IMAGE_SIZE;

        if(imageSize > 0)
            thumbnailWidth = thumbnailWidth*imageSize;

        byte[] imageInByte = null;

        BufferedImage originalBufferedImage = null;
        try {
            originalBufferedImage = ImageIO.read(inputFile);

            int widthToScale, heightToScale;
            if (originalBufferedImage.getWidth() > originalBufferedImage.getHeight()) {

                heightToScale = (int) (1.1 * thumbnailWidth);
                widthToScale = (int) ((heightToScale * 1.0) / originalBufferedImage.getHeight()
                        * originalBufferedImage.getWidth());

            } else {
                widthToScale = (int) (1.1 * thumbnailWidth);
                heightToScale = (int) ((widthToScale * 1.0) / originalBufferedImage.getWidth()
                        * originalBufferedImage.getHeight());
            }

            BufferedImage resizedImage = new BufferedImage(widthToScale,
                    heightToScale, originalBufferedImage.getType());
            Graphics2D g = resizedImage.createGraphics();

            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.drawImage(originalBufferedImage, 0, 0, widthToScale, heightToScale, null);
            g.dispose();

            int x = (resizedImage.getWidth() - thumbnailWidth) / 2;
            int y = (resizedImage.getHeight() - thumbnailWidth) / 2;

            if (x < 0 || y < 0) {
                throw new IllegalArgumentException("Width of new thumbnail is bigger than original image");
            }

            BufferedImage thumbnailBufferedImage = resizedImage.getSubimage(x, y, thumbnailWidth, thumbnailWidth);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnailBufferedImage, "jpg", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        }catch(IOException ex) {
            logger.error("ERROR:: Thumbnail IO exception occurred while trying to read image."+ex);
        }
        return imageInByte;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/shareHolderDetails.do")
    public String shareHolderDetails(Model model) {
        logger.info("********* share Holder Details  Controller ************");

        return "admin/generalConfiguration";
    }

    @RequestMapping(value = "/admin/getShareHolderForAutoComplete.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelForTypeAhead getEmployeeForAutoComplete(HttpServletRequest request) throws Exception {
        logger.info(":: Get share holder For Auto Complete Ajax Controller ::");
        DataModelForTypeAhead dataModelForTypeAhead = new DataModelForTypeAhead();
    /* this params is for dataTables */
        String[] tableColumns = "name,mobile,nationalId".split(",");
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;

       try {
            userDataMap = adminDaoImpl.getShareHolderForAutoComplete(sortColName, sortType, searchKey);

                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            ShareHolder shareHolder = new ShareHolder();
            shareHolder.setShareHolderList((List) userDataMap.get("data"));
            dataModelForTypeAhead.setData(shareHolder);
            dataModelForTypeAhead.setStatus(true);
        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to get shareholder data For Auto Complete:: " + ex);
            dataModelForTypeAhead.setStatus(false);
        }

        return dataModelForTypeAhead;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/allDepositOfSh.do")
    public String allDepositOfShView(Model model, HttpServletRequest request, @RequestParam(value = "shareHolderId", required = true) Integer shareHolderId) {
        logger.info("********* all Deposit Of Share Holder Controller ************");
        System.out.println("shareHolderId::"+shareHolderId);
        ShareHolder shareHolder = new ShareHolder();
        try {
            if (shareHolderId > 0) {
                shareHolder = adminDaoImpl.getShareHolderById(shareHolderId);
                Double allDeposit = adminDaoImpl.getTotalDepositAmount(shareHolderId);
                List<Map> depositList = adminDaoImpl.getDepositListByShareHolderId(shareHolderId);

                model.addAttribute("totalInstallment", depositList != null? depositList.size() : 0);
                model.addAttribute("depositList", depositList);
                model.addAttribute("totalAmount",allDeposit == null? 0: Utils.getTwoDigitAfterDecimal(allDeposit));
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial("<b>"+Utils.getLoggedInUsername()+"</b> "+ Utils.getMessageBundlePropertyValue("audit.total.deposit.view.message")
                        +" '"+shareHolder.getName()+"'", request);
            }
        } catch (Exception e) {
            logger.error("ERROR:" + e);
        }
        model.addAttribute("shareHolder", shareHolder);
        return "admin/allDepositOfSh";
    }

    /*
* Method for viewing shareHolder create/update Page
* @param HttpServletRequest request, Model model
* @return type String( or any .jsp File)
*
*/
    @RequestMapping(value = "/admin/upsertInstallment.do", method = RequestMethod.GET)
    public String upsertInstallment(HttpServletRequest request, Model model) {
        logger.debug("*************** Share Holder get Controller ***************");
        Integer installmentId = request.getParameter("installmentId") != null ? Integer.parseInt(request.getParameter("installmentId")) : 0;
        Installment installment = new Installment();
        try {
            if (installmentId > 0) {
                installment = adminDaoImpl.getInstallmentById(installmentId);
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial("<b>"+Utils.getLoggedInUsername()+"</b> "+ Utils.getMessageBundlePropertyValue("audit.update.installment.view.message")
                        +" '"+installment.getName()+"'", request);

            } else {
                logger.debug("for Save :" + installmentId);
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial("<b>"+Utils.getLoggedInUsername()+"</b> "+Utils.getMessageBundlePropertyValue("audit.new.installment.view.message"), request);
            }
        } catch (Exception e) {
            logger.error("ERROR:" + e);
        }
        model.addAttribute("installment", installment);
        return "admin/installment";
    }

    /*
   * Method for viewing landing Page
   * @param HttpServletRequest request, Model model
   * @return type String( or any .jsp File)
   *
   */
    @RequestMapping(value = "/admin/upsertInstallment.do", method = RequestMethod.POST)
    public String upsertInstallmentPost(HttpServletRequest request, @ModelAttribute("installment") Installment installment, Model model) {

        logger.debug("*************** Upsert Installment POST Controller ***************");
        Integer employeeId = installment.getId();
        try {
            User user = userDaoImpl.getUserByUserName(Utils.getLoggedInUsername());
            installment.setCreatedBy(user);
            installment.setCreated(new Date());
            adminDaoImpl.saveOrUpdate(installment);

            if (employeeId != null){

                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("installment.update.success.msg"));

                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial(Utils.getLoggedInUsername()+" have successfully updated "+" the data of '"+installment.getName()+"'.", request);
            }
            else{

                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("installment.save.success.msg"));
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial(Utils.getLoggedInUsername()+" have successfully saved "+" the data of '"+installment.getName()+"'.", request);
            }
        } catch (Exception ex) {
            logger.error("CERROR:: Failed:" + ex);
            if (installment.getId() != null && installment.getId() > 0) {
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("installment.update.failed.msg")+" "+ex.getMessage());
            } else {
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("installment.save.failed.msg")+" "+ex.getMessage());
            }
        }

        return "redirect:/admin/installmentList.do";
    }

    /*
* Method for viewing installment create/update Page
* @param HttpServletRequest request, Model model
* @return type String( or any .jsp File)
*
*/
    @RequestMapping(value = "/admin/installmentList.do", method = RequestMethod.GET)
    public String installmentListView(HttpServletRequest request, Model model) {
        logger.debug("*************** Installment list Controller ***************");
        try {
            int totalShare = Math.toIntExact(adminDaoImpl.getShareHolderDataSize());
            List<Map> installmentList = adminDaoImpl.getInstallment();
            List<Map> depositList = adminDaoImpl.getDepositListByShareHolderId(null);

            model.addAttribute("totalShare",totalShare);
            model.addAttribute("depositList",depositList);
            model.addAttribute("installmentList",installmentList);
        } catch (Exception e) {
            logger.error("ERROR:" + e);
        }
        return "admin/installmentList";
    }

    /*
* Method for viewing expense create/update Page
* @param HttpServletRequest request, Model model
* @return type String( or any .jsp File)
*
*/
    @RequestMapping(value = "/admin/upsertExpense.do", method = RequestMethod.GET)
    public String upsertExpenseView(HttpServletRequest request, Model model) {
        logger.debug("*************** Upsert Expense Controller ***************");
        Integer expenseId = request.getParameter("expenseId") != null ? Integer.parseInt(request.getParameter("expenseId")) : 0;
        Expense expense = new Expense();

        try {
            if (expenseId > 0) {
                expense = adminDaoImpl.getExpenseById(expenseId);
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial("<b>"+Utils.getLoggedInUsername()+"</b> "+ Utils.getMessageBundlePropertyValue("audit.update.expense.view.message")
                        +" '"+expense.getId()+"'", request);

            } else {
                logger.debug("for Save :" + expenseId);
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial("<b>"+Utils.getLoggedInUsername()+"</b> "+Utils.getMessageBundlePropertyValue("audit.create.expense.message"), request);
            }
            List<ExpenseItem> expenseItemList = adminDaoImpl.getAllExpenseItem();
            model.addAttribute("expenseItemList", expenseItemList);
        } catch (Exception e) {
            logger.error("ERROR:" + e);
        }
        model.addAttribute("expense", expense);
        return "admin/expense";
    }

    /*
* Method for viewing landing Page
* @param HttpServletRequest request, Model model
* @return type String( or any .jsp File)
*
*/
    @RequestMapping(value = "/admin/upsertExpense.do", method = RequestMethod.POST)
    public String upsertShareHolderPost(HttpServletRequest request, @ModelAttribute("expense") Expense expense, Model model) {
        logger.debug("*************** Upsert Expense POST Controller ***************");
        Integer expenseId = expense.getId();
        try {
            User user = userDaoImpl.getUserByUserName(Utils.getLoggedInUsername());
            expense.setCreatedBy(user);
            expense.setDate(new Date());
            adminDaoImpl.saveOrUpdate(expense);

            if (expenseId != null){

                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("expense.update.success.msg"));

                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial(Utils.getLoggedInUsername()+" have successfully updated "+" the data of '"+expense.getId()+"'.", request);
            }
            else{

                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("expense.save.success.msg"));
                // Audit Trial tracking
                adminDaoImpl.saveAuditTrial(Utils.getLoggedInUsername()+" have successfully saved "+" the data of '"+expense.getId()+"'.", request);
            }
        } catch (Exception ex) {
            logger.error("CERROR:: Failed:" + ex);
            if (expense.getId() != null && expense.getId() > 0) {
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("expense.update.failed.msg")+" "+ex.getMessage());
            } else {
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("expense.save.failed.msg")+" "+ex.getMessage());
            }
        }

        return "redirect:/admin/expenseList.do";
    }

    /*
* Method for viewing expense create/update Page
* @param HttpServletRequest request, Model model
* @return type String( or any .jsp File)
*
*/
    @RequestMapping(value = "/admin/expenseList.do", method = RequestMethod.GET)
    public String expenseListList(HttpServletRequest request, Model model) {
        logger.debug("*************** Expense List Controller ***************");

        try {
            List<Expense> expenseList = adminDaoImpl.getAllExpenseList(null, null);
            List<ExpenseItem> expenseItemList = adminDaoImpl.getAllExpenseItem();
            Double totalExpenseAmount = adminDaoImpl.getTotalExpenseAmount(null, null);
            model.addAttribute("expenseList", expenseList);
            model.addAttribute("totalExpenseAmount", Utils.getTwoDigitAfterDecimal(totalExpenseAmount));
            model.addAttribute("expenseItemList", expenseItemList);
        } catch (Exception e) {
            logger.error("ERROR:" + e);
        }
        return "admin/expenseList";
    }
}
