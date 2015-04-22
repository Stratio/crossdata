package com.stratio.crossdata.common.manifest;

import java.util.HashSet;
import java.util.Set;

public class FunctionTypeHelper {

    /**
     * Check the input signature compatibility given a registered input signature.
     * @param storedSignature The string with the registered input signature.
     * @param querySignature The string with the input signature inferred from the function selector.
     * @return A boolean with the check result.
     */
    public static boolean checkInputSignatureCompatibility(String storedSignature, String querySignature) {

        String [] typesInQuerySignature = tokenizeInputSignature(querySignature);
        String [] typesInStoreSignature = tokenizeInputSignature(storedSignature);
        boolean wrongQuerySignatureFound = false;

        int storeIndex = 0;
        int queryIndex = 0;

        while (!wrongQuerySignatureFound && storeIndex < typesInStoreSignature.length && queryIndex < typesInQuerySignature.length){

            String storeType = typesInStoreSignature[storeIndex].trim();
            String queryType = typesInQuerySignature[queryIndex].trim();
            if(!storeType.endsWith("*")){
                if(!storeType.equals("Any")) {
                    if (storeType.equals("")){
                        wrongQuerySignatureFound = !(queryType.equals(storeType) || queryType.endsWith("*"));
                    } else {
                        wrongQuerySignatureFound = !(queryType.equals(storeType)
                                        || queryType.endsWith(storeType + "*")
                                        || checkNumericTypes(queryType, storeType));
                    }
                }
            }else{
                if(!storeType.equals("Any*")) {
                    storeType = storeType.substring(0, storeType.length() - 1);

                    for (int i = queryIndex; i < typesInQuerySignature.length; i++) {
                        if (!(typesInQuerySignature[i].startsWith(storeType) || typesInQuerySignature[i].equals("")
                                || typesInQuerySignature[i].startsWith("Any"))) {
                            wrongQuerySignatureFound = true;
                            break;
                        }
                    }
                }
            }
            queryIndex++;
            storeIndex++;
        }

        return !wrongQuerySignatureFound;
    }

    private static boolean checkNumericTypes(String t1, String t2) {
        Set<String> numericTypes = new HashSet<>();
        numericTypes.add("int");
        numericTypes.add("bigint");
        numericTypes.add("long");
        numericTypes.add("float");
        numericTypes.add("double");

        if(numericTypes.contains(t1.toLowerCase()) && numericTypes.contains(t2.toLowerCase())){
            return true;
        }
        return false;
    }

    private static String[] tokenizeInputSignature(String iSignature) {
        String typesInStoresSignature = iSignature.substring( iSignature.indexOf("Tuple[") + 6, iSignature.indexOf(']'));
        return typesInStoresSignature.split(",");
    }

}
