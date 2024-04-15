package AddressBook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PhoneManagementApp {
	private static String filename = "PhoneDB.txt";

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean isRunning = true;

		System.out.println("***********************************");
		System.out.println("*         전화번호 관리 프로그램         *");
		System.out.println("***********************************");

		while (isRunning) {
			System.out.println("1.리스트  2.등록  3.삭제  4.검색  5.종료");
			System.out.println("-----------------------------------");
			System.out.print(">메뉴번호: ");

			int menuNum = sc.nextInt();

			switch (menuNum) {
			case 1:
				showList();
				break;
			case 2:
				registerPhoneNumber(sc);
				break;
			case 3:
				deletePhoneNumber(sc);
				break;
			case 4:
				searchPhoneNumber(sc);
				break;
			case 5:
				System.out.println("");
				System.out.println("***********************************");
				System.out.println("*            감사합니다              *");
				System.out.println("***********************************");
				isRunning = false;
				break;
			default:
				System.out.println("다시입력해주세요.");
			}
			System.out.println();
		}

		sc.close();
	}

	private static void showList() {
		// 전체 사용자 메뉴 출력
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			System.out.println("<1.리스트>");
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.err.println("파일을 읽어오는 중 오류가 발생했습니다.");
		}
	}

	private static void registerPhoneNumber(Scanner sc) {
		// 등록 기능 구현
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) { // ture : 파일이 이미 존재하는 경우 새로운 내용을 기존 내용 끝에 추가
			System.out.print("이름: ");
			String name = sc.next();
			System.out.print("휴대전화번호: ");
			String mobile = sc.next();
			System.out.print("회사번호: ");
			String company = sc.next();

			bw.write(name + "," + mobile + "," + company);
			bw.newLine();	//	새로운 줄을 만들고 다음에 작성할 텍스트가 새 줄에서 시작
			System.out.println("등록되었습니다.");
		} catch (IOException e) {
			System.err.println("파일에 데이터를 쓰는 중 오류가 발생했습니다.");
		}
	}

	private static void deletePhoneNumber(Scanner sc) {
	    try {
	        File inputFile = new File(filename);
	        //	임시 파일을 부모 디렉토리에 생성하면 작업 중인 파일과 임시 파일을 쉽게 구분할 수 있음
	        //	보안 위험을 최소화
	        //	실행 중인 디렉토리에 대한 권한 문제를 회피
	        File tempFile = new File(inputFile.getParent(), "temp.txt");

	        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
	        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

	        System.out.print("삭제할 이름: ");
	        String nameToDelete = sc.next();
	        String currentLine;

	        boolean found = false;

	        while ((currentLine = reader.readLine()) != null) {	//	파일에서 한 줄씩 읽기
	            // 읽어온 라인에서 이름을 찾아서 삭제(쉼표로 분할하여 배열 저장)
	            String[] parts = currentLine.split(",");
	            if (!parts[0].contains(nameToDelete)) {
	                writer.write(currentLine + System.getProperty("line.separator")); // 시스템에서 사용하는 줄 바꿈 문자열을 반환
	            } else {
	                found = true;
	            }
	        }

	        reader.close();
	        writer.close();

	        if (!found) {
	            System.out.println(nameToDelete + "의 정보가 없습니다.");
	        } else {
	            // 원본 파일 삭제 후 임시 파일을 원본 파일로 변경
	            inputFile.delete();
	            boolean successful = tempFile.renameTo(inputFile); // 파일명 변경 성공시 true
	            if (successful) {
	                System.out.println(nameToDelete + "의 정보가 삭제되었습니다.");
	            } else {
	                System.err.println("파일 이름 변경에 실패했습니다.");
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("파일 처리 중 오류가 발생했습니다.");
	    }
	}

	private static void searchPhoneNumber(Scanner sc) {
		// 검색 기능 구현
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			System.out.print("검색할 이름: ");
			String nameToSearch = sc.next();
			String line;
			boolean found = false;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts[0].contains(nameToSearch)) {
					System.out.println(line);
					found = true;
				}
			}
			if (!found) {
				System.out.println("일치하는 정보가 없습니다.");
			}
		} catch (IOException e) {
			System.err.println("파일을 읽어오는 중 오류가 발생했습니다.");
		}

	}
}
