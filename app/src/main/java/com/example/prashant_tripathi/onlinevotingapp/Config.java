package com.example.prashant_tripathi.onlinevotingapp;

/**
 * Created by Prashant_Tripathi on 28-09-2016.
 */
public class Config {
    public static final String URL_USER_REGISTRATION="http://192.168.0.106/OnlineVoting/RegisterUser.php";
    public static final String URL_ADMIN_REGISTRATION="http://192.168.0.106/OnlineVoting/RegisterAdmin.php";
    public static final String URL_CANDIDATE_REGISTRATION="http://192.168.0.106/OnlineVoting/RegisterCandidate.php";
    public static final String URL_ADD_ELECTION="http://192.168.0.106/OnlineVoting/AddElectionDetails.php";
    public static final String URL_ALL_ELECTIONS="http://192.168.0.106/OnlineVoting/ShowElections.php?elec_name=";
    public static final String URL_ALL_CANDIDATES="http://192.168.0.106/OnlineVoting/ShowCandidates.php?elec_name=";
    public static final String URL_DROP_ELECTION="http://192.168.0.106/OnlineVoting/DropElection.php";
    public static final String URL_CREATE_ELECTION="http://192.168.0.106/OnlineVoting/CreateElection.php";
    public static final String URL_DELETE_ELECTION="http://192.168.0.106/OnlineVoting/DeleteElection.php";
    public static final String URL_DELETE_CANDIDATE="http://192.168.0.106/OnlineVoting/DeleteCandidate.php";
    public static final String URL_SHOW_RESULTS="http://192.168.0.106/OnlineVoting/ShowResults.php?elec_name=";
    public static final String URL_ELECTION_DETAILS="http://192.168.0.106/OnlineVoting/EditElection.php?elec_name=";
    public static final String URL_LOAD_DISTRICTS="http://192.168.0.106/OnlineVoting/LoadDistricts.php?state=";

   // public static final String URL_ALL_USERS="http://192.168.0.106/ChatterBox/ChatRoom.php?user=";
   // public static final String URL_GET_USER="http://192.168.0.106/ChatterBox/Login.php?user=";
   // public static final String URL_MESSAGE="http://192.168.0.106/ChatterBox/Messages.php";

    // public static final String URL_UPDATE_EMP="http://100.73.140.166/Android/CRUD/updateEmp.php";
    //  public static final String URL_DELETE_EMP="http://100.73.140.166/Android/CRUD/deleteEmp.php?id=";

    public static final String KEY_FIRST_NAME="first";
    public static final String KEY_MIDDLE_NAME="mid";
    public static final String KEY_LAST_NAME="last";
    public static final String KEY_AADHAR="aadhar";
    public static final String KEY_EMPID="empid";
    public static final String KEY_DOB="dob";
    public static final String KEY_STATE="state";
    public static final String KEY_DISTRICT="district";
    public static final String KEY_ADDRESS1="address1";
    public static final String KEY_ADDRESS2="address2";
    public static final String KEY_GENDER="gender";
    public static final String KEY_EMAIL="email";
    public static final String KEY_USERNAME="username";
    public static final String KEY_PASSWORD="pass";
    public static final String KEY_ELECTION_NAME="elec_name";
    public static final String KEY_ELECTION_STATE="elec_state";
    public static final String KEY_ELECTION_DATE="elec_date";
    public static final String KEY_CANDIDATE_NAME="candi_name";
    public static final String KEY_CANDIDATE_PARTY="candi_party";
    public static final String KEY_CANDIDATE_VOTES="candi_votes";

    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_DISTRICT="district";
    public static final String TAG_STATE="state";



      public static final String EMP_ID="emp_id";
}
