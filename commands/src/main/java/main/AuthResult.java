package main;


public class AuthResult extends DataPackage
    {

        private final Result result;

        private AuthResult(Result result)
        {
            this.result = result;
        }


        public Result getResult()
        {
            return result;
        }


        public static AuthResult ok()
        {
            return new AuthResult(Result.OK);
        }


        public static AuthResult fail()
        {
            return new AuthResult(Result.FAIL);
        }


        public enum Result
        {
            FAIL,
            OK
        }

}
