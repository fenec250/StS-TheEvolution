package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.ShadowGene2;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.NightMarePower;

import java.util.ArrayList;

public class NightMare4
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:NightMare";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/NightmareP.png";
    private static final int COST = 2;
    private static final int POWER_AMT = 2;
    private static final int UPGRADE_COST = 1;

    public NightMare4() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = POWER_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new NightMarePower(p,this.magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new NightMare4();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
        }
    }
}