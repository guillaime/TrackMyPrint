package org.fontys.trackmyprint;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.fontys.trackmyprint.database.entities.Product;

public class PrintDetailsActivity extends AppCompatActivity {

    private ImageView imageFront;
    private ImageView imageBack;
    private TextView lblInfo1;
    private TextView lblInfo2;
    private TextView lblInfo3;
    private TextView lblInfo4;
    private TextView lblPhase;
    private ImageView imgPhaseIcon;

    private MainActivity mInstance;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_details);

        mInstance = MainActivity.getInstance();
        currentProduct = mInstance.getCurrentProduct();

        this.imageFront = (ImageView) findViewById(R.id.imgFrontImage);
        this.imageBack = (ImageView) findViewById(R.id.imgBackImage);
        this.imgPhaseIcon = (ImageView) findViewById(R.id.imgPhaseIcon);
        this.lblInfo1 = (TextView) findViewById(R.id.lblInfo1);
        this.lblInfo2 = (TextView) findViewById(R.id.lblInfo2);
        this.lblInfo3 = (TextView) findViewById(R.id.lblInfo3);
        this.lblPhase = (TextView) findViewById(R.id.lblPhase);

        Context c = getApplicationContext();

        imgPhaseIcon.setImageResource(getResources().getIdentifier(("drawable/" + mInstance.getCurrentPhase().getId()), null, c.getPackageName()));
        imageFront.setImageResource(getResources().getIdentifier(("drawable/" + currentProduct.getImageFront()), null, c.getPackageName()));
        imageBack.setImageResource(getResources().getIdentifier(("drawable/" + currentProduct.getImageBack()), null, c.getPackageName()));
        lblPhase.setText(mInstance.getCurrentPhase().getName());


        switch (mInstance.getCurrentPhase().getName()) {
            case "Prepairing":
                imgPhaseIcon.setImageResource(R.drawable.quality_control_oval);
                setViewPrepairing();
                break;
            case "Printing":
                imgPhaseIcon.setImageResource(R.drawable.printer_oval);
                setViewPrinting();
                break;
            case "Cutting":
                imgPhaseIcon.setImageResource(R.drawable.cutting_oval);
                setViewCutting();
                break;
            case "Quality control":
                imgPhaseIcon.setImageResource(R.drawable.quality_control_oval);
                setViewQualityControl();
        }


    }

    private void setViewPrepairing() {
        lblInfo1.setText("Paper size: " + currentProduct.getPaperSize());
        lblInfo2.setText("Paper color: " + currentProduct.getPaperColor());
    }

    private void setViewPrinting() {
        lblInfo1.setText("Paper size: " + currentProduct.getPaperSize());
        lblInfo2.setText("Paper color: " + currentProduct.getPaperColor());
        lblInfo3.setText("Print amount: " + currentProduct.getAmount() + "x");
    }

    private void setViewCutting() {
        lblInfo1.setText("Margin top: " + currentProduct.getMarginTop() + "; Margin bottom: " + currentProduct.getMarginBottom());
        lblInfo2.setText("Margin left: " + currentProduct.getMarginLeft() + "; Margin right: " + currentProduct.getMarginRight());
    }

    private void setViewQualityControl() {
        lblInfo1.setText("Paper size: " + currentProduct.getPaperSize());
        lblInfo2.setText("Paper color: " + currentProduct.getPaperColor());
        lblInfo3.setText("Print amount: " + currentProduct.getAmount() + "x");
    }

}
