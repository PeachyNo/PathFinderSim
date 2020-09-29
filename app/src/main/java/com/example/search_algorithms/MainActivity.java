package com.example.search_algorithms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private IconNodeQueue pathQueue;
    private IconNodeQueue shortestQueue;

    int[] images = {R.drawable.unpressed_button, R.drawable.pressed_button,
            R.drawable.searched_button, R.drawable.path_button,
            R.drawable.start_button, R.drawable.finish_button};

    String[] algorithmList = new String[]{"Depth-First Search", "Breadth-First Search", "Greedy",
            "A-Star"};
    String[] speedList = new String[]{"    1    ", "    2    ", "    3    "};



    private GridLayout pathGrid;
    private IconNode[][] iconNodes;

    private int iconMode;
    private int brows;
    private int bcolumns;
    private int startx;
    private int starty;
    private int finishx;
    private int finishy;
    private int search_mode;
    private boolean canPress;
    private int currentSpeed;

    private SearchQueue currentSearch;

    private Spinner algorithmSpinner;
    private Spinner speedChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pathQueue = new IconNodeQueue();
        shortestQueue = new IconNodeQueue();
        currentSearch = new SearchQueue();
        search_mode = Values.DEPTH_FIRST;
        canPress = true;

        iconMode = Values.ICON_TYPE_WALL;
        startx = -1;
        starty = -1;
        finishx = -1;
        finishy = -1;

        currentSpeed = 100;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        // find grid
        pathGrid = (GridLayout) findViewById(R.id.path_grid);
        // Calculate number of buttons in grid
        brows = width / 180;
        bcolumns = height / 180 - 3;


        // TODO remove static grid creation
        iconNodes = new IconNode[bcolumns][];

        //get the spinner from the xml.
        algorithmSpinner = findViewById(R.id.search_algorithm_choice);
        //create a list of items for the spinner.
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, algorithmList);
        //set the spinners adapter to the previously created one.
        algorithmSpinner.setAdapter(adapter);
        algorithmSpinner.setOnItemSelectedListener(this);

        speedChoice = findViewById(R.id.speed_choice);
        //create a list of items for the spinner.
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, speedList);
        //set the spinners adapter to the previously created one.
        speedChoice.setAdapter(adapter2);
        speedChoice.setOnItemSelectedListener(this);


        for (int i = 0;  i < bcolumns; i++){
            iconNodes[i] = new IconNode[brows];
            for (int j = 0; j < brows; j++){
                ImageButton newButton = new ImageButton(MainActivity.this);
                newButton.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                ));

                newButton.setImageResource(images[Values.ICON_TYPE_CLEAR]);
                newButton.setPadding(0,0,0,0);

                newButton.setId((i*brows) + j);

                pathGrid.addView(newButton);
                iconNodes[i][j] = new IconNode(newButton);
                newButton.setId(i*brows+j);

                newButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {

                                    ImageButton buttonOne = (ImageButton) view;
                                    int j = buttonOne.getId();
                                    int i = buttonOne.getId();
                                    i /= brows;
                                    j %= brows;
                                    //if (v.getId() == R.id.button0_0) {
                                    if (iconMode == Values.ICON_TYPE_START) {
                                        if (iconNodes[i][j].icon_type == iconMode){
                                            startx = -1;
                                            starty = -1;
                                            final Animation anima = AnimationUtils.loadAnimation(MainActivity.this, R.anim.unpressed);
                                            buttonOne.startAnimation(anima);
                                            buttonOne.setImageResource(images[Values.ICON_TYPE_CLEAR]);
                                            iconNodes[i][j].icon_type = Values.ICON_TYPE_CLEAR;
                                        }
                                        else {
                                            if(startx != -1 && starty != -1){
                                                // TODO change current start icon
                                                IconNode n = iconNodes[starty][startx];
                                                ImageButton button = (ImageButton) n.getButton();
                                                final Animation anima = AnimationUtils.loadAnimation(MainActivity.this, R.anim.unpressed);
                                                button.startAnimation(anima);
                                                button.setImageResource(images[Values.ICON_TYPE_CLEAR]);
                                                n.icon_type = Values.ICON_TYPE_CLEAR;
                                            }
                                            startx = j;
                                            starty = i;
                                            buttonOne.setImageResource(images[iconMode]);
                                            iconNodes[i][j].icon_type = iconMode;
                                            final Animation anima = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pressed);
                                            buttonOne.startAnimation(anima);
                                        }
                                    }
                                    else if (iconMode == Values.ICON_TYPE_FINISH){
                                        if (iconNodes[i][j].icon_type == iconMode){
                                            finishx = -1;
                                            finishy = -1;
                                            final Animation anima = AnimationUtils.loadAnimation(MainActivity.this, R.anim.unpressed);
                                            buttonOne.startAnimation(anima);
                                            buttonOne.setImageResource(images[Values.ICON_TYPE_CLEAR]);
                                            iconNodes[i][j].icon_type = Values.ICON_TYPE_CLEAR;
                                        }
                                        else {
                                            if(finishx != -1 && finishy != -1){
                                                // TODO change current start icon
                                                IconNode n = iconNodes[finishy][finishx];
                                                ImageButton button = (ImageButton) n.getButton();
                                                final Animation anima = AnimationUtils.loadAnimation(MainActivity.this, R.anim.unpressed);
                                                button.startAnimation(anima);
                                                button.setImageResource(images[Values.ICON_TYPE_CLEAR]);
                                                n.icon_type = Values.ICON_TYPE_CLEAR;
                                            }
                                            finishx = j;
                                            finishy = i;
                                            buttonOne.setImageResource(images[iconMode]);
                                            iconNodes[i][j].icon_type = iconMode;
                                            final Animation anima = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pressed);
                                            buttonOne.startAnimation(anima);
                                        }
                                    }
                                    else if (iconNodes[i][j].icon_type == iconMode){
                                        final Animation anima = AnimationUtils.loadAnimation(MainActivity.this, R.anim.unpressed);
                                        buttonOne.startAnimation(anima);
                                        buttonOne.setImageResource(images[Values.ICON_TYPE_CLEAR]);
                                        iconNodes[i][j].icon_type = Values.ICON_TYPE_CLEAR;
                                    }
                                    else {
                                        if (iconNodes[i][j].icon_type == Values.ICON_TYPE_START){
                                            startx = -1;
                                            starty = -1;
                                        }
                                        buttonOne.setImageResource(images[iconMode]);
                                        iconNodes[i][j].icon_type = iconMode;
                                        final Animation anima = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pressed);
                                        buttonOne.startAnimation(anima);
                                    }

                                    int k = 0;
                                     while (k < 20){
                                         //buttonOne.setImageResource(images[k%images.length]);
                                         wait(1);
                                         k++;
                                     }
                                    //}
                                } catch (Exception e){
                                    // TODO add exception print
                                }
                            }
                        }
                );
            }
        }


        // set button event
        //buttonClick();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (canPress) {
            switch (itemThatWasClickedId) {
                case R.id.wall:
                    iconMode = Values.ICON_TYPE_WALL;
                    return true;
                case R.id.finish:
                    iconMode = Values.ICON_TYPE_FINISH;
                    return true;
                case R.id.start:
                    iconMode = Values.ICON_TYPE_START;
                    return true;
                case R.id.simulate:
                    // TODO start simulation here
                    // TODO make it so that there can only be one start
                    // going to for now assume start is at 0,0
                    // find start icon

                    clearPath();
                    currentSearch.clear();
                    if (startx != -1) {
                        switch (search_mode) {
                            case Values.DEPTH_FIRST:
                                disableGrid();
                                canPress = false;
                                depthFirstSearch(starty, startx);
                                break;
                            case Values.BREADTH_FIRST:
                                disableGrid();
                                canPress = false;
                                breadthFirstSearch(starty, startx);
                                break;
                            case Values.GREEDY:
                                disableGrid();
                                canPress = false;
                                greedySearch(starty, startx);
                                break;
                            case Values.ASTAR:
                                disableGrid();
                                canPress = false;
                                aStar(starty, startx);
                                break;
                        }

                        new CountDownTimer(currentSpeed * pathQueue.getSize(), 100) {
                            public void onTick(long millisUntilFinished) {
                                while (pathQueue != null && pathQueue.getSize() > 0 &&
                                        ((long)(pathQueue.getSize() * currentSpeed) > millisUntilFinished)) {
                                    displayNewIcon(pathQueue.pop());
                                }
                            }

                            public void onFinish() {
                                while (pathQueue != null && pathQueue.getSize() > 0) {
                                    displayNewIcon(pathQueue.pop());
                                }

                                new CountDownTimer(currentSpeed * shortestQueue.getSize(), 100) {
                                    public void onTick(long millisUntilFinished) {
                                        while (shortestQueue != null && shortestQueue.getSize() > 0 &&
                                                ((long)(shortestQueue.getSize() * currentSpeed) > millisUntilFinished)) {
                                            displayNewIcon(shortestQueue.pop(), Values.ICON_TYPE_PATH);
                                        }
                                    }

                                    public void onFinish() {
                                        while (shortestQueue != null && shortestQueue.getSize() > 0) {
                                            displayNewIcon(shortestQueue.pop());

                                        }
                                        enableGrid();
                                        canPress = true;
                                    }
                                }.start();
                            }
                        }.start();
                    }

                    return true;
                case R.id.clear_grid:
                    clearGrid();
                    return true;
                case R.id.clear_path:
                    clearPath();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void disableGrid(){
        for(int i = 0; i < iconNodes.length;i++){
            for(int j = 0; j < iconNodes[i].length;j++){
                iconNodes[i][j].getButton().setEnabled(false);
            }
        }
    }

    private void enableGrid(){
        for(int i = 0; i < iconNodes.length;i++){
            for(int j = 0; j < iconNodes[i].length;j++){
                iconNodes[i][j].getButton().setEnabled(true);
            }
        }
    }

    public void clearGrid(){
        for (int i = 0; i < iconNodes.length; i++){
            for (int j = 0; j < iconNodes[i].length; j++){
                iconNodes[i][j].getButton().setImageResource(images[Values.ICON_TYPE_CLEAR]);
                iconNodes[i][j].icon_type = Values.ICON_TYPE_CLEAR;
            }
        }
    }

    public void clearPath(){
        for (int i = 0; i < iconNodes.length; i++){
            for (int j = 0; j < iconNodes[i].length; j++){
                switch (iconNodes[i][j].icon_type){
                    case Values.ICON_TYPE_PATH:
                    case Values.ICON_TYPE_SEARCHED:
                        iconNodes[i][j].getButton().setImageResource(images[Values.ICON_TYPE_CLEAR]);
                        iconNodes[i][j].icon_type = Values.ICON_TYPE_CLEAR;
                        break;
                }
            }
        }

    }


    public void displayNewIcon(IconNode n){
        ImageButton b = n.getButton();
        b.setImageResource(images[n.icon_type]);
        final Animation anima = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pressed);
        b.startAnimation(anima);
    }

    public void displayNewIcon(IconNode n, int newIcon){
        ImageButton b = n.getButton();
        n.icon_type = newIcon;
        b.setImageResource(images[newIcon]);
        final Animation anima = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pressed);
        b.startAnimation(anima);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.search_algorithm_choice:
                search_mode = i;
                break;
            case R.id.speed_choice:
                currentSpeed = (100+(i*i*100));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // TODO not really sure what to do here either
    }


    // Depth first search will start from the top and work its way clockwise
    private boolean depthFirstSearch(int y, int x){
        if (y < 0 || x < 0 || y >= bcolumns || x >= brows)
            return false;

        IconNode n = iconNodes[y][x];
        ImageButton button = (ImageButton) n.getButton();

        switch (n.icon_type){
            case Values.ICON_TYPE_START:
                if (depthFirstSearch(y-1,x) ||
                        depthFirstSearch(y, x + 1) ||
                        depthFirstSearch(y + 1, x) ||
                        depthFirstSearch(y, x - 1)){
                    return true;
                }
                break;
            case Values.ICON_TYPE_CLEAR:
                //displaySearched(n);
                pathQueue.add(n);
                n.icon_type = Values.ICON_TYPE_SEARCHED;
                if (depthFirstSearch(y-1,x) ||
                depthFirstSearch(y, x + 1) ||
                depthFirstSearch(y + 1, x) ||
                depthFirstSearch(y, x - 1)){

                    //displayPath(n);
                    //R.id.path_grid += 200;
                    shortestQueue.add(n);
                    return true;
                }
                break;

            case Values.ICON_TYPE_FINISH:
                return true;
        }


        return false;
    }

    private boolean breadthFirstSearch(int y, int x){
        currentSearch.add(y,x,new IconNodeQueue());

        while(currentSearch.getSize() > 0){
            int cy = currentSearch.getY();
            int cx = currentSearch.getX();
            IconNode n = iconNodes[cy][cx];
            ImageButton button = (ImageButton) n.getButton();

            switch (n.icon_type){
                case Values.ICON_TYPE_START:
                    if(checkAroundNode(cy,cx)){
                        return true;
                    }
                    break;
                case Values.ICON_TYPE_SEARCHED:
                case Values.ICON_TYPE_CLEAR:
                    //displaySearched(n);
                    if(checkAroundNode(cy,cx)){
                        // TODO create a working shortest finder shortestQueue.add(n);
                        return true;
                    }
                    break;
            }

            currentSearch.pop();
        }
        return false;
    }
    private boolean checkAroundNode(int y, int x){
        IconNode n = iconNodes[y][x];
        int[] check = {-1,0,0,1,1,0,0,-1};

        int i = 0;

        while(i < 8){
            if((y+check[i])<bcolumns && (y+check[i])>=0
            && (x+check[i+1])<brows && (x+check[i+1])>=0){
                IconNode next = iconNodes[y+check[i]][x+check[i+1]];
                switch (next.icon_type){
                    case Values.ICON_TYPE_FINISH:
                        shortestQueue = currentSearch.getPathCopy();
                        return true;
                    case Values.ICON_TYPE_CLEAR:
                        pathQueue.add(next);
                        IconNodeQueue cpy = currentSearch.getPathCopy();
                        cpy.add(next);
                        currentSearch.add(y+check[i],x+check[i+1], cpy);
                        next.icon_type = Values.ICON_TYPE_SEARCHED;
                        break;
                }
            }
            i+=2;
        }
        return false;
    }

    private boolean greedySearch(int y, int x){
        if (y < 0 || x < 0 || y >= bcolumns || x >= brows)
            return false;

        IconNode n = iconNodes[y][x];
        ImageButton button = (ImageButton) n.getButton();

        switch (n.icon_type){
            case Values.ICON_TYPE_START:
                if (greedyHelper(y, x)){
                    return true;
                }
                break;
            case Values.ICON_TYPE_CLEAR:
                //displaySearched(n);
                pathQueue.add(n);
                n.icon_type = Values.ICON_TYPE_SEARCHED;


                if (greedyHelper(y,x)){
                    shortestQueue.add(n);
                    return true;
                }
                break;

            case Values.ICON_TYPE_FINISH:
                return true;
        }
        return false;
    }

    private boolean greedyHelper(int y, int x){
        SearchQueue leftOver = new SearchQueue();
        IconNode n = iconNodes[y][x];
        int[] check = {-1,0,0,1,1,0,0,-1};

        int i = 0;

        while(i < 8){
            int checkx = x+check[i+1];
            int checky = y+check[i];
            if((difference(x,finishx) > difference(checkx, finishx)) ||
                    (difference(y,finishy) > difference(checky, finishy)) ) {
                if(checky<bcolumns && checky>=0
                        && checkx<brows && checkx>=0){
                    IconNode next = iconNodes[y + check[i]][x + check[i + 1]];
                    switch (next.icon_type) {
                        case Values.ICON_TYPE_FINISH:
                            return true;
                        case Values.ICON_TYPE_CLEAR:
                            if(greedySearch(checky,checkx)) return true;
                            break;
                    }
                }
            }
            else{
                leftOver.add(checky,checkx,null);
            }
            i+=2;
        }

        while(leftOver.getSize()>0){
            int checkx = leftOver.getX();
            int checky = leftOver.getY();
            if(checky<bcolumns && checky>=0
                    && checkx<brows && checkx>=0){
                IconNode next = iconNodes[checky][checkx];
                switch (next.icon_type) {
                    case Values.ICON_TYPE_FINISH:
                        return true;
                    case Values.ICON_TYPE_CLEAR:
                        if (greedySearch(checky,checkx)) return true;
                        break;
                }
            }
            leftOver.pop();
        }


        return false;
    }

    static public int difference(int a, int b){
        if(a > b){
            return a - b;
        }
        return b-a;
    }

    public boolean aStar(int y, int x){
        PriorityQueue priorityQueue = new PriorityQueue();
        priorityQueue.add(y,x,distance(y,x,starty,startx),new IconNodeQueue());

        while(priorityQueue.size > 0){
            int cy = priorityQueue.getHY();
            int cx = priorityQueue.getHX();
            IconNodeQueue prevPath = priorityQueue.getPrevQueue().copy();
            priorityQueue.pop();
            IconNode n = iconNodes[cy][cx];
            switch (n.icon_type){
                case Values.ICON_TYPE_CLEAR:
                    pathQueue.add(n);
                    n.icon_type = Values.ICON_TYPE_SEARCHED;
                    prevPath.add(n);
                case Values.ICON_TYPE_START:
                    int[] check = {-1,0,0,1,1,0,0,-1};
                    int i = 0;
                    while(i < 8) {

                        int checkx = cx + check[i + 1];
                        int checky = cy + check[i];
                        if (checky < bcolumns && checky >= 0 && checkx < brows && checkx >= 0) {
                            IconNode next = iconNodes[checky][checkx];
                            if(next.icon_type != Values.ICON_TYPE_SEARCHED &&
                                    next.icon_type != Values.ICON_TYPE_START) {
                                priorityQueue.add(checky, checkx, distance(checky, checkx,
                                        finishy, finishx) + prevPath.getSize(), prevPath.copy());
                            }
                        }
                        i+=2;
                    }
                    break;
                case Values.ICON_TYPE_FINISH:
                    shortestQueue = prevPath;
                    return true;
            }
        }
        return false;
    }

    static public int distance(int ay, int ax, int by, int bx){
        return (difference(ax,bx) + difference(ay,by));
    }

    public boolean dijkstra(int y, int x){
        return false;
    }
}
