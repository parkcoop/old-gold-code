public class ScannerModule extends ReactContextBaseJavaModule{

  private final  int REQUEST_ECODE_SCAN=1;
  private Promise mPromise;
  private ReactApplicationContext mContext;

  public ScannerModule(ReactApplicationContext reactContext) {
      super(reactContext);
      mContext=reactContext;
      mContext.addActivityEventListener(mActivityEventListener );
  }

  @Override
  public String getName() {
      return "ScannerModule";
  }

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
      @Override
      public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
          if(mPromise!=null&&requestCode==REQUEST_ECODE_SCAN){
              if (resultCode == Activity.RESULT_CANCELED) {
                  mPromise.reject("300", "Image picker was cancelled");
              }else if(resultCode==RESULT_OK&&data.getData()!=null){
                  String  result=data.getStringExtra("codedContent");
                  mPromise.resolve(result);

              }else{
                  mPromise.reject("203","QR code recognition failed, please try again later");
              }
              mPromise=null;
          }
      }
  };

  @ReactMethod
  public void requestEcoderScan(final Promise promise){
      this.mPromise=promise;
      Activity activity=getCurrentActivity();

      if (activity == null) {
          mPromise.reject("E_ACTIVITY_DOES_NOT_EXIST", "Activity doesn't exist");
          return;
      }
      try{
          Intent intent=new Intent(activity, CaptureActivity.class);
          activity.startActivityForResult(intent,REQUEST_ECODE_SCAN);
      }catch (Exception e){
          mPromise.reject("START_ACTIVITY_ERROR", "" +
                  "START_ACTIVITY_ERROR");
          mPromise=null;
      }

  }


}