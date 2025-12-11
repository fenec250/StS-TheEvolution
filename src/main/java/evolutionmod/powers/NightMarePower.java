package evolutionmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.CrystalDust;
import evolutionmod.cards.CrystalShard;
import evolutionmod.cards.CrystalShield;
import evolutionmod.orbs.*;

import java.util.Iterator;

public class NightMarePower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:NightMarePower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public NightMarePower(AbstractCreature owner, int initialAmount) {
//        this.geneId = geneId;
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/TheNightPower84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/TheNightPower32.png"), 0, 0, 32, 32);
        this.loadRegion("focus");
        this.type = PowerType.BUFF;
        this.amount = initialAmount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void atStartOfTurn() {
        int vigor = (GameActionManager.turn+1) * this.amount;
        addToBot(new ApplyPowerAction(this.owner, this.owner, new VigorPower(this.owner, vigor)));
        super.atStartOfTurn();
    }
}
