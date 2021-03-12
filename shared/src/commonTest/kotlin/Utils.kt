import com.elevenetc.zipher.shared.containsIdx

fun verify(result: String): (Char) -> Unit {
    var idx = 0
    return {

        if (!result.containsIdx(idx)) {
            throw RuntimeException("result($result) doesn't contain idx($idx)")
        } else if (result[idx] != it) {
            throw RuntimeException("received char($it) at index($idx) is not equal result char(${result[idx]})")
        }

        idx++
    }
}