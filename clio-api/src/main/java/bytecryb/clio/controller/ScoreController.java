package bytecryb.clio.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import bytecryb.clio.model.CustomUser;
import bytecryb.clio.model.ResultScore;
import bytecryb.clio.model.Score;
import bytecryb.clio.repository.ScoreRepository;
import bytecryb.clio.repository.UserRepository;
import bytecryb.clio.util.JwtUtil;

@RestController
@RequestMapping("/api/v1")
public class ScoreController {
	@Autowired
	private ScoreRepository scoreRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtUtil jwtUtil;

	/*
	
	// GET DAILY
	@GetMapping("/daily")
	public ResponseEntity<List<ResultScore>> getDailyTopScores(@RequestParam(name = "day") int day, @RequestParam(name = "month") int month, @RequestParam(name = "year") int year, HttpServletRequest request) {

		
		List<Score> query = this.scoreRepo.findAll(Sort.by("day").descending());
		List<ResultScore> result = new ArrayList<ResultScore>();

		String jwtToken = extractJwtFromRequest(request);
		String username = jwtUtil.getUsernameFromToken(jwtToken);
		CustomUser curUser = this.userRepo.findByUsername(username);
		List<Score> userScore = this.scoreRepo.findByUserId(curUser.getUserId());
		result.add(new ResultScore(0, username, userScore.getDay()));

		Iterator<Score> scoreIterator = query.iterator();
    
		int i = 1;
		while (scoreIterator.hasNext() && i < 10) {
			Score tmp = scoreIterator.next();
			long tmpUserId = tmp.getUserId();
			CustomUser user = this.userRepo.findById(tmpUserId);
			result.add(new ResultScore(i, user.getUsername(), tmp.getDay()));
			i++;
		}
		return ResponseEntity.ok().body(result);
	}


	// GET MONTHLY
	@GetMapping("/month")
	public ResponseEntity<List<ResultScore>> getMonthlyTopScores(@RequestParam(name = "month") int month, @RequestParam(name = "year") int year, HttpServletRequest request) {
		System.out.println(month + year);
		List<Score> query = this.scoreRepo.findAll(Sort.by("month").descending());
		List<ResultScore> result = new ArrayList<ResultScore>();

		String jwtToken = extractJwtFromRequest(request);
		String username = jwtUtil.getUsernameFromToken(jwtToken);
		CustomUser curUser = this.userRepo.findByUsername(username);
		List<Score> userScore = this.scoreRepo.findByUserId(curUser.getUserId());
		result.add(new ResultScore(0, username, userScore.getMonth()));

		Iterator<Score> scoreIterator = query.iterator();

		int i = 1;
		while (scoreIterator.hasNext() && i < 10) {
			Score tmp = scoreIterator.next();
			long tmpUserId = tmp.getUserId();
			CustomUser user = this.userRepo.findById(tmpUserId);
			result.add(new ResultScore(i, user.getUsername(), tmp.getMonth()));
			i++;
		}
		return ResponseEntity.ok().body(result);
	}

	*/

	// GET TOP MONTHLY
	@GetMapping("/scores/month")
	public ResponseEntity<List<Object[]>> getMonthlyTopScores() {

		List<Object[]> result = new ArrayList<>();
		result = this.scoreRepo.findAllMonthlyScores();


		return ResponseEntity.ok().body(result);
	}

	// GET ALL TIME
	@GetMapping("/scores/alltime")
	public ResponseEntity<List<ResultScore>> getAllTimeTopScores(HttpServletRequest request) {
		
		List<ResultScore> result = new ArrayList<ResultScore>();

		// Getting current user's all time score
		String jwtToken = extractJwtFromRequest(request);
		String username = jwtUtil.getUsernameFromToken(jwtToken);
		CustomUser curUser = this.userRepo.findByUsername(username);
		List<Score> userScores = this.scoreRepo.findByUserId(curUser.getUserId());
		int total = 0;
		for (Score s : userScores) {
			total += s.getScore();
		}
		result.add(new ResultScore(0, username, total));

		List<Score> query = this.scoreRepo.findAll(Sort.by("score").descending());
		Iterator<Score> scoreIterator = query.iterator();

		int i = 1;
		while (scoreIterator.hasNext() && 1 < 10) {
			Score tmp = scoreIterator.next();
			long tmpUserId = tmp.getUserId();
			CustomUser user = this.userRepo.findById(tmpUserId);
			result.add(new ResultScore(i, user.getUsername(), tmp.getScore()));
			i++;
		}
		return ResponseEntity.ok().body(result);
	}

	private String extractJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	/*
	Select EmployeeID, SUM(InvoiceAmount) 
	From Calls
	Group by EmployeeID
	*/
}
