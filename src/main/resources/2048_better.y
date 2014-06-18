fluent maxV = {0};
fluent directionV = {0};
fluent limitStep = {2};

fluent row1 = {};
fluent row2 = {};
fluent row3 = {};
fluent row4 = {};

fluent highVal = {0};
fluent step = {0};

action stepUp($a)
    effect: 
        $nextStep = $a + 1;
        step = {$nextStep};
end action

action resetStep() 
    effect:
        step = {0};
end action

action resetMaxV() 
    effect:
        maxV = {0};
end action

action resetHighVal() 
    effect:
        highVal = {0};
end action

action print($a)
    signal: 
        $a
end action

action resetRows()
    effect:
       row1 = {};
       row2 = {};
       row3 = {};
       row4 = {};
end action

action initRow1($val)
    effect:
       row1 += {$val};
end action

action initRow2($val)
    effect:
       row2 += {$val};
end action

action initRow3($val)
    effect:
       row3 += {$val};
end action

action initRow4($val)
    effect:
       row4 += {$val};
end action

action setDirectionV($dir)
    effect:
        directionV = {$dir};
end action

action setMaxV($max)
    effect:
        maxV = {$max};
end action

action setHighVal($val)
    effect:
        highVal = {$val};
end action

proc procInitRow($val11, $val12, $val13, $val14, $val21, $val22, $val23, $val24, $val31, $val32, $val33, $val34, $val41, $val42, $val43, $val44)
    resetRows();

    initRow1($val11);
    initRow1($val12);
    initRow1($val13);
    initRow1($val14);

    initRow2($val21);
    initRow2($val22);
    initRow2($val23);
    initRow2($val24);

    initRow3($val31);
    initRow3($val32);
    initRow3($val33);
    initRow3($val34);

    initRow4($val41);
    initRow4($val42);
    initRow4($val43);
    initRow4($val44);
end proc

proc procShowResult()
    for $val in directionV do
        print($val);
    end for
    for $val in maxV do
        print($val);
    end for
end proc

proc procStepUp()
    for $val in step do
        stepUp($val);
    end for
end proc

proc procEvaluate($val)
    if (highVal == {$val} and highVal > maxV)
    then 
        setMaxV($val);
        if step <= limitStep
        then setDirectionV(0);
        else setDirectionV(1);
        end if
    end if
    setHighVal($val);
end proc

proc procEvaluateDirection()
    resetMaxV();
    resetHighVal();
    resetStep();

    for $val in row1 do
        procEvaluate($val);
        procStepUp();
    end for
    resetHighVal();
    resetStep();

    for $val in row2 do
        procEvaluate($val);
        procStepUp();
    end for
    resetHighVal();
    resetStep();

    for $val in row3 do
        procEvaluate($val);
        procStepUp();
    end for
    resetHighVal();
    resetStep();

    for $val in row4 do
        procEvaluate($val);
        procStepUp();
    end for

    procShowResult();
end proc
