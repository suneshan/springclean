package za.co.sunesh.springclean;

import org.springframework.stereotype.Component;
import za.co.sunesh.springclean.pojos.Address;
import za.co.sunesh.springclean.pojos.Result;
import za.co.sunesh.springclean.utils.CommonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

@Component
public class Controller {

    public int gcd(int[] numbers) {
        // check input params are valid i.e. more than one val and does not contain a 0 value
        // if not valid throw an exception
        if (numbers.length == 1) {
            throw new RuntimeException("Enter 2 or more non-zero integer numbers");
        } else if(Arrays.stream(numbers).anyMatch(number->number==0)) {
            throw new RuntimeException("Values must be non-zero and between -2147483648 and 2147483647");
        }

        // apply euclidean algorithm
        int tmp = numbers[0];
        numbers[0] = abs(numbers[1]);
        int modVal = tmp % numbers[1];
        // end recursive loop when the mod of two number return 0 and there are no further numbers in the array
        if (modVal == 0) {
            if(numbers.length-1 > 1) {
                // remove the processed number and repeat for remaining numbers
                return gcd(IntStream.range(0, numbers.length) .filter(i -> i != 1).map(i -> numbers[i]).toArray());
            }
            return numbers[0];
        }
        numbers[1] = modVal;
        return gcd(numbers);
    }

    public String prettyPrintAddress(Address address) {
        final String SEPARATOR = " - ";
        final String SPACE = " ";
        if (address != null) {
            StringBuilder sb = new StringBuilder();
            if (address.getType() != null) sb.append(address.getType().getName());
            sb.append(SEPARATOR);
            if (address.getAddressLineDetail() != null) {
                sb.append(address.getAddressLineDetail().getLine1()).append(SPACE);
                sb.append(address.getAddressLineDetail().getLine2());
            }
            sb.append(SEPARATOR);
            sb.append(address.getCityOrTown()).append(SEPARATOR);
            if (address.getProvinceOrState() != null) sb.append(address.getProvinceOrState().getName());
            sb.append(SEPARATOR);
            sb.append(address.getPostalCode()).append(SEPARATOR);
            if (address.getCountry() != null) sb.append(address.getCountry().getName());

            return sb.toString();
        }
        return null;
    }

    public void printAllAddressesFromJsonFile() {
        List<Address> addresses = CommonUtils.jsonStringToAddressList(CommonUtils.readFileFromResource("addresses.json"));
        addresses.forEach(address -> {
            System.out.println(prettyPrintAddress(address));
        });
    }

    public void printAddressOfType(String type) {
        if (type != null && type.isEmpty() == false) {
            List<Address> addresses = CommonUtils.jsonStringToAddressList(CommonUtils.readFileFromResource("addresses.json"));
            addresses.stream().filter(address -> address.getType() != null && type.equals(address.getType().getName())).forEach(address -> {
                System.out.println(prettyPrintAddress(address));
            });
        }
    }

    public Result isValidAddress(Address address) {
        Result result = new Result();
        if (address == null) {
            result.appendError("No address object found.");
        } else {
            if (CommonUtils.isInteger(address.getPostalCode()) == false)
                result.appendError("Postal code must be numeric");

            if (address.getCountry() == null || address.getCountry().getName() == null ||
                    address.getCountry().getName().isEmpty())
                result.appendError("Address must contain a country");

            if (address.getAddressLineDetail() == null ||
                    ((address.getAddressLineDetail().getLine1() == null || address.getAddressLineDetail().getLine1().isEmpty()) &&
                            address.getAddressLineDetail().getLine2() == null || address.getAddressLineDetail().getLine2().isEmpty()))
                result.appendError("Must have at least one address line that is not blank or null");

            if ("ZA".equals(address.getCountry().getCode()) && (address.getProvinceOrState() == null || address.getProvinceOrState().getName() == null
                    || address.getProvinceOrState().getName().isEmpty()))
                result.appendError("South African country requires a province");
        }
        return result;
    }

    public void validateJsonFileAddresses() {
        List<Address> addresses = CommonUtils.jsonStringToAddressList(CommonUtils.readFileFromResource("addresses.json"));
        addresses.forEach(address -> {
            Result result = isValidAddress(address);
            if (result.isValid()) {
                System.out.format("* Address ID: %s is valid%n", address.getId());
            } else {
                System.out.format("* Address ID: %s is invalid due to:%n", address.getId());
                result.getErrorMessages().forEach(System.out::println);
            }
        });
    }
}
