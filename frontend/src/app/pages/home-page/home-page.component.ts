import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { PostRequest } from 'src/app/payloads/request/post';
import { PostResponse } from 'src/app/payloads/response/post';
import { AuthService } from 'src/app/services/auth.service';
import { PostService } from 'src/app/services/post.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit, OnDestroy {

  navigationSubscription: Subscription;
  newTweetForm: FormGroup;
  payload: PostRequest;
  tweets: Array<PostResponse>;
  username: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private toastr: ToastrService,
    private postService: PostService,
    private router: Router,
    private authService: AuthService
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false;
    };

    this.navigationSubscription = this.router.events.subscribe((event: any) => {
      if (event instanceof NavigationEnd) {
        this.router.navigated = false;
      }
    });

    this.username = this.authService.getUsername();

    this.newTweetForm = new FormGroup({
      text: new FormControl("", Validators.required)
    });
    this.tweets = [];
    this.payload = {
      text: "",
      type: ""
    };
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      if (params["signedIn"] !== undefined && params["signedIn"] === "true") {
        this.toastr.success("Sign In Successful");
      }
    });
    this.fetchTweets();
  }

  fetchTweets() {
    this.postService.getAll().subscribe({
      next: (data: PostResponse[]) => {
        this.tweets = data;
      }
    });
  }

  ngOnDestroy() {
    if (this.navigationSubscription) {
      this.navigationSubscription.unsubscribe();
    }
  }

  tweet() {
    this.payload.text = this.newTweetForm.get("text")?.value;
    this.payload.type = "TWEET";

    this.postService.tweet(this.payload).subscribe({
      next: (response: any) => {
        console.log(response);
        this.newTweetForm.reset();
        this.router.navigateByUrl("home");
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }
}
