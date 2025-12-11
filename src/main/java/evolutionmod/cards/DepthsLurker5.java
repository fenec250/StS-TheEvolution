package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.Iterator;

public class DepthsLurker5
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:DepthsLurker";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/DepthsLurker.png";
    private static final int COST = 1;
    private static final int WEAK_AMT = 3;

    public DepthsLurker5() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        this.magicNumber = this.baseMagicNumber = WEAK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!this.upgraded) {
            addToBot(new ApplyPowerAction(m, p, new WeakPower(p, this.magicNumber, false)));
        }
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        while (var3.hasNext()) {
            AbstractMonster mo = (AbstractMonster) var3.next();
            if (this.upgraded)
                this.addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, this.magicNumber, false), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    if (!mo.isDeadOrEscaped() && mo.hasPower(WeakPower.POWER_ID))
                        addToTop(new MerfolkGene().getChannelAction());
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DepthsLurker5();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.target = CardTarget.ALL;
            this.initializeDescription();
        }
    }
}