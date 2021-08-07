package pro.sanjagh.lamoa.domain

object Functions {
  def distance(s1: String, s2: String): Double = {
    val s1_length = s1.length + 1
    val s2_length = s2.length + 1
    val matrix = Array.ofDim[Int](s1_length, s2_length)

    for (i <- 0.until(s1_length)) {
      matrix(i)(0) = i
    }
    for (j <- 0.until(s2_length)) {
      matrix(0)(j) = j
    }

    var cost = 0
    for (j <- 1.until(s2_length)) {
      for (i <- 1.until(s1_length)) {
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
          cost = 0
        } else {
          cost = 1
        }
        matrix(i)(j) = math.min(
          math.min(matrix(i - 1)(j) + 1, matrix(i)(j - 1) + 1),
          matrix(i - 1)(j - 1) + cost
        )
      }
    }
    matrix(s1_length - 1)(s2_length - 1)
  }
}
