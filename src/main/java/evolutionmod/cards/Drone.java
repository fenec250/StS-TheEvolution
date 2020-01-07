package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.patches.AbstractCardEnum;

public class Drone
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:Drone";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 0;
    private static final int UPGRADE_COST = 1;
    private static final int DAMAGE_AMT = 3;
    private static final int UPGRADE_DAMAGE_AMT = 5;
    private static final int UPGRADE_BLOCK_AMT = 6;

    public Drone() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.BASIC, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.exhaust = true;
        this.isEthereal = true;
//        this.tags.add(DRONE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        }
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.useAdaptations(p, m);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        // if player has InsectForm, add its amount to damage.
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.updateCost(UPGRADE_COST);
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.exhaust = false;
            this.isEthereal = false;
            this.block = this.baseBlock = UPGRADE_BLOCK_AMT;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}