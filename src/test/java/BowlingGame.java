package test.java;

public class BowlingGame {

	public static int getBowlingScore(String s) {
		if (s == null || s.length() == 0)
			return 0;
		String[] strInit = s.split("\\|\\|"); // strInit has two elements,strInit[0]:the first ten frames; strInit[1]:the last(the 11th) frame
		String[] frame = strInit[0].split("\\|"); // frame[0]~frame[9]:the first ten frames
		int sum = 0;
		int frameNum = 10;
		int fullScore = 10;
		int totalScore = 0;
		int[] frameScore = new int[frameNum + 1];
		int[] flag = new int[frameNum]; // flag = 1 means spare; flag=2 means strike
		
		/*
		 * get the value of frameScore's last element
		 */
		if (strInit.length == 1) { // the 11th frame doesn't exist
			frameScore[frameScore.length - 1] = 0;
		} else { // the 11th exists
			if (strInit[1].endsWith("/")) {
				sum = fullScore;
			} else {
				char[] chs = strInit[1].toCharArray();
				for (int i = 0; i < chs.length; i++) {
					if (chs[i] == '-') {
						sum += 0;
					} else if (chs[i] == 'X') {
						sum += fullScore;
					} else { // [1-9]
						sum += Integer.parseInt(String.valueOf(chs[i]));
					}
				}
			}
			frameScore[frameScore.length - 1] = sum;
		}

		/*
		 * get the initial value of frameScore and get flag
		 */
		for (int i = 0; i < frameNum; i++) {
			if (frame[i].length() == 1) {
				if (frame[i].matches("X")) {
					frameScore[i] = fullScore;
					flag[i] = 2;
				} else { // if a frame is "X" then it must have only one char
					throw new RuntimeException("Wrong Input:Invalid Format!");
				}
			} else if (frame[i].length() == 2) {
				if (frame[i].matches("[1-9]/") || frame[i].matches("-/")) {
					frameScore[i] = fullScore;
					flag[i] = 1;
				} else if (frame[i].matches("[1-9]-")) {
					frameScore[i] = Integer.parseInt(frame[i].split("-")[0]);
					flag[i] = 0;
				} else if (frame[i].matches("-[1-9]")) {
					frameScore[i] = Integer.parseInt(frame[i].split("-")[1]);
					flag[i] = 0;
				} else if (frame[i].matches("[1-9][1-9]")) {
					char[] chs = frame[i].toCharArray();
					frameScore[i] = Integer.parseInt(String.valueOf(chs[0]))
							+ Integer.parseInt(String.valueOf(chs[1]));
					flag[i] = 0;
				} else if (frame[i].matches("--")) {
					frameScore[i] = 0;
					flag[i] = 0;
				} else { // if a frame has two chars,it must be consist of
							// '-','[1-9]'and'/'
					throw new RuntimeException("Wrong Input:Invalid Format!");
				}
			} else { // if a frame has more than two chars,it must be wrong
				throw new RuntimeException("Wrong Input:No More than Two!");
			}
		}

		/*
		 * get the final value of frameScore's first eight elements
		 */
		for (int i = 0; i <= frameNum - 3; i++) {
			if (flag[i] == 2) { // if strike, present frameScore should add the
								// next two times' score
				if (frame[i + 1].length() == 2) {
					frameScore[i] += frameScore[i + 1];
				} else if (frame[i + 1].length() == 1) { // next frame is a "X",its length is 1
					frameScore[i] += fullScore;
					if (frame[i + 2].length() == 1 && frame[i + 2].matches("X")) { // the next two frame is also a "X"
						frameScore[i] += fullScore;
					} else if (frame[i + 2].length() == 2) { // the next two frame is not a "X"
						char[] chs = frame[i + 2].toCharArray();
						frameScore[i] += (chs[0] == '-' ? 0 : Integer
								.parseInt(String.valueOf(chs[0])));
					}
				}
			} else if (flag[i] == 1) { // if spare, present frameScore should add the next time's score
				if (frame[i + 1].length() == 1) {
					frameScore[i] += fullScore;
				} else if (frame[i + 1].length() == 2) {
					char[] chs = frame[i + 1].toCharArray();
					frameScore[i] += (chs[0] == '-' ? 0 : Integer
							.parseInt(String.valueOf(chs[0])));
				}
			}
		}

		/*
		 * get the final value of frameScore's 9th element
		 */
		int i = frameNum - 2;
		if (flag[i] == 2) {
			if (frame[i + 1].length() == 1 && frame[i + 1].matches("X")) {
				frameScore[i] += fullScore;
				if (strInit[1].matches("X")) {
					frameScore[i] += fullScore;
				} else if (strInit[1].length() == 2) {
					if (strInit[1].matches("[1-9][1-9]")) {
						frameScore[i] += Integer.parseInt(String
								.valueOf(strInit[1].charAt(0)));
					} else if (strInit[1].startsWith("X")) {
						frameScore[i] += fullScore;
					} else {
						frameScore[i] += strInit[1].charAt(0) == '-' ? 0
								: Integer.parseInt(String.valueOf(strInit[1]
										.charAt(0)));
					}
				}
			} else { // next frame is not a "X"
				frameScore[i] += frameScore[i + 1];
			}
		} else if (flag[i] == 1) {
			if (frame[i + 1].length() == 1 && frame[i + 1].matches("X")) {
				frameScore[i] += fullScore;
			} else { // next frame is not a "X", it has two chars
				frameScore[i] += frame[i + 1].charAt(0) == '-' ? 0 : Integer
						.parseInt(String.valueOf(frame[i + 1].charAt(0)));
			}
		}

		/*
		 * get the final value of frameScore's 10th element
		 */
		i = frameNum - 1;
		frameScore[i] += frameScore[frameScore.length - 1];

		/*
		 * get totalScore
		 */
		for (int k = 0; k < frameNum; k++) {
			totalScore += frameScore[k];
		}
		return totalScore;
	}
}
